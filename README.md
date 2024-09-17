# RAG Chat
RAG application for .pdf and .txt files

## Packages Installation
```
pip install \
    llama-index \
    huggingface_hub[hf_transfer] \
    sentence-transformers \
    torch \
    chromadb \
    fastapi \
    python-dotenv \
    python-multipart \
    PyPDF2
```

## Download Local Embedding Model
```
HF_HUB_ENABLE_HF_TRANSFER=1 huggingface-cli download \
    dunzhang/stella_en_1.5B_v5 \
    --local-dir models/dunzhang/stella_en_1.5B_v5
```

## Start Server Process
```
python3 app.py
```