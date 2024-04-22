import os
from dotenv import load_dotenv
import spotipy
from spotipy.oauth2 import SpotifyOAuth
import json

# Load environment variables from .env
load_dotenv()

# Define your Spotify API credentials
client_id = os.getenv("CLIENT_ID")
client_secret = os.getenv("CLIENT_SECRET")
redirect_uri = os.getenv("REDIRECT_URI", "http://localhost:8888/callback")
scope = "user-top-read"

def init_spotify_client():
    """
    Initialize the Spotipy client with client credentials.
    """
    sp = spotipy.Spotify(auth_manager=SpotifyOAuth(client_id=client_id, client_secret=client_secret, redirect_uri=redirect_uri, scope=scope, show_dialog=True))
    return sp

def get_top_tracks(sp):
    """
    Get the top 10 tracks of the user with their artists and genres.
    """
    try:
        top_tracks = sp.current_user_top_tracks(limit=10, time_range='short_term')
        if top_tracks:
            playlist_tracks = []
            for track in top_tracks['items']:
                # Check if artists exist and are not None
                if track['artists']:
                    artists_info = []
                    for artist in track['artists']:
                        if artist is not None:
                            artist_id = artist['id']
                            artist_name = artist['name']
                            # Get the genres for this artist
                            genres = get_artist_genres(sp, artist_id)
                            artists_info.append({
                                "name": artist_name,
                                "genres": genres
                            })
                    playlist_tracks.append({
                        "name": track['name'],
                        "artists": artists_info
                    })
            return playlist_tracks
        else:
            print("No top tracks found.")
            return []
    except spotipy.SpotifyException as e:
        print("Error:", e)
        return []


def get_artist_genres(sp, artist_id):
    """
    Get the genres of an artist.
    """
    try:
        artist = sp.artist(artist_id)
        if artist and 'genres' in artist:
            return artist['genres']
        else:
            return []
    except spotipy.SpotifyException as e:
        print("Error getting artist genres:", e)
        return []

if __name__ == "__main__":
    # Initialize Spotipy client
    sp = init_spotify_client()

    # Get top 10 tracks of the user with artists and genres
    top_tracks = get_top_tracks(sp)

    if top_tracks:
        # Write JSON to a file
        with open('top_tracks_with_artists_and_genres.json', 'w') as json_file:
            json.dump(top_tracks, json_file, indent=4)

        print("Top 10 tracks with artists and genres from the user's top tracks have been written to top_tracks_with_artists_and_genres.json file.")
    else:
        print("Failed to retrieve top tracks.")
