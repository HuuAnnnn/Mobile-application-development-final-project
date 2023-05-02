from fastapi import FastAPI
from .routers import users
from dotenv import load_dotenv
from pathlib import Path
from .database.MongoDB import MongoDB
import os

CURR_PATH = Path(__file__)
ENV_PATH = os.path.join(CURR_PATH.parent, "credentials", ".env")
load_dotenv(ENV_PATH, override=True)

app = FastAPI()
mongoDB = MongoDB()
app.include_router(users.router)


@app.get("/")
async def root():
    return {"message": "Connect successfully"}
