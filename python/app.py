from flask import Flask, jsonify, request
from appv2_backend import init_spotify_client, get_top_tracks
from dotenv import load_dotenv
from langchain_core.output_parsers import StrOutputParser
from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import ChatOpenAI
import os
import logging

logging.basicConfig(filename='server.log', level=logging.DEBUG)

# Load environment variables from .env
load_dotenv()

app = Flask(__name__)

# Load the JSON data
folder_path = '/Users/kasikritc/Documents/Georgia Tech/Academics/2024-Spring/CS 2340/spotifyWrapped-brin/python'
json_file_path = '/playlist_tracks_with_artists_and_genres.json'
with open(folder_path + json_file_path, 'r') as file:
    json_text = file.read()

# Set up OpenAI chat
os.environ['OPENAI_API_KEY'] = "sk-proj-7O0t2XjljlKiTfu20d7vT3BlbkFJyIOY2JsWyMqaOvUnHdAF"
model = ChatOpenAI(model="gpt-3.5-turbo")
output_parser = StrOutputParser()

# # Endpoint to get user's top 10 tracks with artists and genres
# @app.route('/api/user/top-tracks', methods=['GET'])
# def user_top_tracks():
#     print("Received request for user's top tracks")
    
#     # Initialize Spotipy client
#     sp = init_spotify_client()

#     # Get top 10 tracks with artists and genres
#     top_tracks = get_top_tracks(sp)

#     # Return the top tracks as JSON
#     return jsonify(top_tracks)

@app.route('/test')
def test():
    return 'test'

# Define Flask routes for each analysis task
@app.route('/api/user/actions')
def analyze_user_actions():
    music_taste = request.args.get('music_taste', '')
    logging.debug("Received music_taste: %s", music_taste)  # Corrected logging statement
    user_act_prompt_template = """
    << user's music taste >>
    {music_taste}.

    << prompt >>
    Analyze the genres, playlists, top tracks, and artists mentioned to discern patterns or themes. 
    Use these insights to describe, in a brief and focused manner, the user's probable daily habits, 
    lifestyle choices, and personality traits. Consider the emotional, cultural, and social implications of their music preferences. 
    Summarize your findings in 3 sentences that highlight how their music taste reflects on their actions and preferences in daily life.
    """
    user_act_prompt = ChatPromptTemplate.from_template(user_act_prompt_template)
    user_act_chain = user_act_prompt | model | output_parser
    return user_act_chain.invoke({'music_taste': music_taste})

@app.route('/api/user/thoughts')
def analyze_user_thoughts():
    user_think_prompt_template = """
    << user's music taste >> 
    {music_taste}.

    << prompt >> 
    Analyze the genres, playlists, top tracks, and artists mentioned to discern patterns or themes. 
    Use these insights to infer, in a brief and focused manner, the user's thought processes, problem-solving approaches, and emotional responses. 
    Consider the psychological and philosophical implications of their music preferences. 
    Summarize your findings in 3 sentences that highlight how their music taste may influence their mindset, values, and outlook on life.
    """
    user_think_prompt = ChatPromptTemplate.from_template(user_think_prompt_template)
    user_think_chain = user_think_prompt | model | output_parser
    result = user_think_chain.invoke({'music_taste': json_text})
    return jsonify(result)

@app.route('/api/user/dress')
def analyze_user_dress():
    user_dress_prompt_template = """
    << user's music taste >> 
    {music_taste}.

    << prompt >> 
    Examine the genres, playlists, top tracks, and artists mentioned to identify underlying style influences. 
    Leverage these insights to conjecture, concisely and pointedly, about the user's fashion sense, preferred types of attire, and potential influences on their personal grooming and aesthetic choices. 
    Reflect on how their music preferences could translate into visual and stylistic elements of dress. 
    Summarize your observations in 3 sentences that illustrate how their music taste is likely mirrored in their fashion choices and presentation in daily life.
    """
    user_dress_prompt = ChatPromptTemplate.from_template(user_dress_prompt_template)
    user_dress_chain = user_dress_prompt | model | output_parser
    result = user_dress_chain.invoke({'music_taste': json_text})
    return jsonify(result)

if __name__ == '__main__':
    # Run the Flask app on port 8888
    app.run(host="0.0.0.0", debug=True, port=8888)
