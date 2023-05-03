from fastapi import FastAPI
from .routers import users
from dotenv import load_dotenv
from pathlib import Path
from .database.MongoDB import MongoDB
import os

app = FastAPI()
app.include_router(users.router)


@app.get("/")
async def root():
    return {"message": "Connect successfully"}
