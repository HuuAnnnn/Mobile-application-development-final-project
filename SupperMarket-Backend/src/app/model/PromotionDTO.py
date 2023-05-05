from pydantic import BaseModel


class PromotionDTO(BaseModel):
    title: str
    dateExpired: str
    code: str
