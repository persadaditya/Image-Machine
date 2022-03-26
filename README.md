# Image-Machine

## History
Nowadays, people tend to generate tons of images daily and it becomes harder and harder to group all those images and manage them efficiently.
Here comes the image grouping application that will help ease people’s lives.

### Key Feature
this App Feature consist of:
- Persistence data with SQLite
- List with view
- Scan QR Code
- Check Sort with help of SharedPreferences
- Pick Image from app folder
- Display Image

### Architecture and Library
This project architecture use mvvm for arch and go with several library like
- Persistence with Room Library
- Scan QR with yuriy-budiyev based on Zxing Lib
- Pick Image with tedImagePicker Lib
- Display Image with Glide

### Structure Folder
- main folder
  - base (for base application, this we initialize our database, session, and repo)
  - data (our data)
    - dao (our data access object, how we query our local data)
    - db (our database and converter here)
    - model (our entity table for our database)
    - repo (our repo how we get data from our dao)
    - Session Handler (our shared preference to keep some data)
  - ui (our view and viewmodel is here)
  - Util (All of our util, esp Date and Currency for later)

### Screen
- main (this is our main: this is our machine list, add button, sort button, and qr button here)
- addedit (this is our add and edit machine, add image based our machine-id)
- detail (this is our machine detail, consist of machine information and images with edit and delete button)
- scan (this is our scanner view, consist of machine information if there is in our local database)
- ImageActivity (this is our fullscreen image)


#### Clarify
- random id used with UUID
- saving image I go with path because of some reason
  - there is too much data if we convert to blob or base64
  - easy for getting high res
  cons : if image is deleted or moved and so on the image in our app is gone because we just save the path. 
