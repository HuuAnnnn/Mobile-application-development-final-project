from fastapi import FastAPI
from .routers import users, product, notification

app = FastAPI()
app.include_router(users.router)
app.include_router(product.router)
app.include_router(notification.router)


@app.get("/")
async def root():
    return {"message": "Connect successfully"}
