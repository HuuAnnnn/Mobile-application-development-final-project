from fastapi import FastAPI
from .routers import users, product, notification, receipt, promotion

app = FastAPI()
app.include_router(users.router)
app.include_router(product.router)
app.include_router(notification.router)
app.include_router(receipt.router)
app.include_router(promotion.router)


@app.get("/")
async def root():
    return {"message": "Connect successfully"}
