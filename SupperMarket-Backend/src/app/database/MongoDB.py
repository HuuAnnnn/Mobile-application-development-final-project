from pymongo import MongoClient
from os import getenv
from dotenv import load_dotenv


class MongoDB:
    def __init__(self) -> None:
        self.mongo_client = MongoClient(getenv("MONGO_CONNECTION_STR", ""))
        print("Load db successfully")

    def get_mongo_client(self, db_name: str):
        return self.mongo_client[db_name]
