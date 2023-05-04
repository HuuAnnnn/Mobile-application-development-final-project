from pydantic import BaseModel


class AddToCart(BaseModel):
    username: str
    product_id: str
    quantity: int
