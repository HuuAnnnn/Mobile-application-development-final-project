from pymongo import MongoClient
from os import getenv
from dotenv import load_dotenv
import os
from pathlib import Path

class MongoDB:
    def __init__(self) -> None:
        CURR_PATH = Path(__file__)
        ENV_PATH = os.path.join(CURR_PATH.parent, "..", "credentials", ".env")
        load_dotenv(ENV_PATH, override=True)
        self.mongo_client = MongoClient(getenv("MONGO_CONNECTION_STR", ""))
        print("Load db successfully")

    def get_mongo_client(self, db_name: str):
        return self.mongo_client[db_name]
