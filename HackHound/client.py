import streamlit as st
import requests
from PIL import Image
import io
import json

# FastAPI endpoint URLs
IMAGE_TEXT_API_URL = "http://localhost:8000/analyze"
SAVE_API_URL = "http://192.168.137.123:3000/save"

def get_image_text_response(image_bytes, text_query):
    """Calls the image and text API endpoint and returns the Gemini response."""
    try:
        files = {"file": ("image.jpg", image_bytes, "image/jpeg")}
        data = {"text_query": text_query}
        response = requests.post(IMAGE_TEXT_API_URL, files=files, data=data)
        response.raise_for_status()
        return response.json()  # Return the JSON response directly
    except requests.exceptions.RequestException as e:
        return {"error": f"Error connecting to image/text API: {e}"}

def save_response_to_db(json_data):
    """Sends the JSON data to the /save endpoint."""
    try:
        response = requests.post(SAVE_API_URL, json=json_data)
        response.raise_for_status()
        return {"save_result": response.json()}  # Return the save result
    except requests.exceptions.RequestException as e:
        return {"save_error": f"Error saving to database: {e}"}

st.title("Gemini API Testing")

# Image and Text Query
st.subheader("Image and Text Query")
uploaded_file = st.file_uploader("Upload an image:", type=["jpg", "jpeg", "png"])
image_text_query = st.text_input("Enter text related to the image:")

if st.button("Get Image and Text Response"):
    if uploaded_file is not None:
        try:
            image_bytes = uploaded_file.getvalue()
            # Debugging: Print the query before sending it
            print(f"Query before sending: '{image_text_query}'")

            if image_text_query and image_text_query.strip(): #check if query is not empty after removing whitespace.
                print("Using user-provided query")
                gemini_response = get_image_text_response(image_bytes, image_text_query)
            else:
                print("Using default query")
                gemini_response = get_image_text_response(image_bytes, "genrate a json object in which there will the be the key which are shown in investigation and the result as the value for that key and give the title of image as object name .")

            if "error" in gemini_response:
                st.error(gemini_response["error"])
            else:
                st.write("Gemini Response:")
                st.write(gemini_response)

                # Send the response to the /save endpoint
                save_response = save_response_to_db(gemini_response)

                if "save_error" in save_response:
                    st.error(save_response["save_error"])
                else:
                    st.write("Save Response:")
                    st.write(save_response["save_result"])

                image = Image.open(io.BytesIO(image_bytes))
                st.image(image, caption="Uploaded Image", use_container_width=True)

        except Exception as e:
            st.error(f"Error: {e}")
    else:
        st.warning("Please upload an image.")