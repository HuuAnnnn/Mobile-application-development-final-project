from pydantic import BaseModel


class NotificationDTO(BaseModel):
    title: str
    content: str
    image_url: str
