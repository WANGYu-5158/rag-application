from fastapi import FastAPI, HTTPException, UploadFile, File, Form
from fastapi.responses import JSONResponse, HTMLResponse
from fastapi.staticfiles import StaticFiles
from dotenv import load_dotenv
from openai import AzureOpenAI
from pydantic import BaseModel
from typing import List, Sequence
from llama_index.core.node_parser import SentenceSplitter
from PyPDF2 import PdfReader
import os
import chromadb
import chromadb.utils.embedding_functions
import torch
import io

app = FastAPI()

app.mount("/static", StaticFiles(directory="static"), name="static")

load_dotenv()

llm = AzureOpenAI(
    api_key=os.getenv("AZURE_API_KEY"),
    api_version=os.getenv("AZURE_API_VERSION"),
    azure_endpoint=os.getenv("AZURE_ENDPOINT"),
)
MODEL_NAME=os.getenv("AZURE_MODEL_NAME")
TEMPERATURE=0.0

EMBED_MODEL="dunzhang/stella_en_1.5B_v5"
CHUNK_SIZE=3072
CHUNK_OVERLAP=int(0.2 * CHUNK_SIZE)
SIMILARITY_TOP_K=6
DISTANCE_METRIC="cosine"

# class UploadRequest(BaseModel):
#     files: List[UploadFile] = File(...)
#     file_id: str
#     db_id: str

@app.post("/upload")
# async def upload_files(request: UploadRequest):
async def upload_files(
    files: List[UploadFile] = File(...),
    file_id: str = Form(...),
    db_id: str = Form(...),
    ):
    db = chromadb.PersistentClient(path="./chroma_db")

    collection_params = {
        "name": "chroma_collection",
        "embedding_function": chromadb.utils.embedding_functions.SentenceTransformerEmbeddingFunction(
            model_name=EMBED_MODEL,
            device=torch.device("cuda" if torch.cuda.is_available() else "cpu")
        )
    }

    try:
        chroma_collection = db.get_collection(**collection_params)
    except Exception:
        collection_params["metadata"] = {"hnsw:space": DISTANCE_METRIC}
        chroma_collection = db.create_collection(**collection_params)
    
    sentence_splitter = SentenceSplitter(chunk_size=CHUNK_SIZE, chunk_overlap=CHUNK_OVERLAP)
    
    documents = []
    metadatas = []
    ids = []

    for uploaded_file in files:
        file_content = await uploaded_file.read()
        file_extension = uploaded_file.filename.split('.')[-1].lower()
        
        if file_extension == 'pdf':
            pdf_reader = PdfReader(io.BytesIO(file_content))
            text = ''
            for page in pdf_reader.pages:
                text += page.extract_text()
        elif file_extension == 'txt':
            text = file_content.decode('utf-8')
        else:
            continue
        
        chunks = sentence_splitter.split_text(text)

        for chunk_id, chunk in enumerate(chunks):
            documents.append(chunk)
            metadatas.append({
                "file_name": uploaded_file.filename,
                "file_id": file_id,
                "db_id": db_id
            })
            ids.append(f"{db_id}_{file_id}")
    
    chroma_collection.add(
        documents=documents,
        metadatas=metadatas,
        ids=ids
    )
    
    return {"message": "Files uploaded successfully"}

class ChatRequest(BaseModel):
    user_input: str

class ChatResponse(BaseModel):
    answer: str
    sources: List[str]

@app.post("/chat_script", response_model=ChatResponse)
def chat(request: ChatRequest):
    user_input = request.user_input
    db = chromadb.PersistentClient(path="./chroma_db")

    collection_params = {
        "name": "chroma_collection",
        "embedding_function": chromadb.utils.embedding_functions.SentenceTransformerEmbeddingFunction(
            model_name=EMBED_MODEL,
            device=torch.device("cuda" if torch.cuda.is_available() else "cpu")
        )
    }

    try:
        chroma_collection = db.get_collection(**collection_params)
    except Exception:
        raise HTTPException(status_code=404, detail="Collection not found")
    
    QueryResults = chroma_collection.query(
        query_texts=user_input,
        n_results=SIMILARITY_TOP_K,
        include = ["documents", "metadatas"]
    )
    contexts: Sequence[str] = QueryResults['documents'][0] if QueryResults['documents'][0] else [""]
    metadatas: Sequence[dict] = QueryResults['metadatas'][0] if QueryResults['metadatas'][0] else []

    if contexts == [""]:
        raise HTTPException(status_code=404, detail="No context retrieved")
    
    system_prompt = f"""Context information is below.
    ---------------------
    {contexts}
    ---------------------"""

    user_prompt = f"""Given the context information and not prior knowledge, answer the query.
    Query: {user_input}
    Answer: """

    response = llm.chat.completions.create(
        model=MODEL_NAME,
        temperature=TEMPERATURE,
        messages=[
            {"role": "system", "content": system_prompt},
            {"role": "user", "content": user_prompt}
        ]
    )
    answer = response.choices[0].message.content

    sources = [metadata['file_name'] for metadata in metadatas]

    return ChatResponse(
        answer=answer,
        sources=sources
    )

class DeleteRequest(BaseModel):
    db_id: str
    file_id: str

class DeleteResponse(BaseModel):
    success: bool

@app.post("/delete", response_model=DeleteResponse)
def delete(request: DeleteRequest):
    db = chromadb.PersistentClient(path="./chroma_db")

    collection_params = {
        "name": "chroma_collection",
        "embedding_function": chromadb.utils.embedding_functions.SentenceTransformerEmbeddingFunction(
            model_name=EMBED_MODEL,
            device=torch.device("cuda" if torch.cuda.is_available() else "cpu")
        )
    }

    try:
        chroma_collection = db.get_collection(**collection_params)
    except Exception:
        collection_params["metadata"] = {"hnsw:space": DISTANCE_METRIC}
        chroma_collection = db.create_collection(**collection_params)

    try:
        chroma_collection.delete(
            ids=f"{request.db_id}_{request.file_id}"
        )
    except Exception:
        return DeleteResponse(
            success=False
        )

    return DeleteResponse(
        success=True
    )


@app.get("/", response_class=HTMLResponse)
def read_root():
    with open("static/upload.html", "r") as f:
        return f.read()

@app.get("/chat", response_class=HTMLResponse)
def read_chat():
    with open("static/chat.html", "r") as f:
        return f.read()

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)