from fastapi import FastAPI
from .routers import users, product

app = FastAPI()
app.include_router(users.router)
app.include_router(product.router)


@app.get("/")
async def root():
    return {"message": "Connect successfully"}
