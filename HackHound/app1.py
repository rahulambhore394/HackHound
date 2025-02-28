from fastapi import FastAPI, UploadFile, File, HTTPException
from google import genai
from PIL import Image
import io
import json
import requests

app = FastAPI()

# Initialize the Gemini client (replace with your actual API key)
client = genai.Client(api_key="AIzaSyBzEg9vjzDDA-dnrjauXBzz1nmdwPV-ojA")

def process_image_and_text(image_bytes: bytes, input_text: str):
    """Processes the image and text using the Gemini model."""
    try:
        image = Image.open(io.BytesIO(image_bytes))
        response = client.models.generate_content(
            model="gemini-2.0-flash",
            contents=[image, input_text]
        )
        return response.text
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/analyze/")
async def analyze_image(file: UploadFile = File(...), query: str = "genrate a json object in which there will the be the key which are shown in investigation and the result as the value for that key and give the title of image as object name ."):
    """Analyzes an uploaded image with a given query."""
    try:
        image_bytes = await file.read()
        result = process_image_and_text(image_bytes, query)
        try:
            # Attempt to parse the response as JSON.
            json_result = json.loads(result)

            # Send the JSON data to the specified endpoint
            try:
                requests.post("http://192.168.137.123:3000/save", json=json_result)
            except requests.exceptions.RequestException as post_error:
                print(f"Error sending JSON to /save: {post_error}")

            return json_result
        except json.JSONDecodeError:
            # If JSON parsing fails, return the raw text.
            return {"result": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="localhost", port=8000)