# TV Show Renaming

The purpose of this software is to rename shows from common but varied initial formats to formats that Plex prefers (eg. 'Show e1 Episode.Title.hd.1080p.mkv' to 'Show - S01E01 - Episode Title.mkv')

Supported features:
* Retaining the episode name
* Replacing characters in episode name
* Removing characters after the episode name
* Missing seasons
* Handling 1 vs 01 in the episode number
* Preview of an example renaming with confirmation

How to use:
0. Download the program and place it in the same directory as the complete TV show. (eg. were I renaming episodes for Scrubs I'd place it next to the folder named 'Scrubs').
1. Compile the program - 'javac TVShowRenaming.java'
2. Run the program - 'java TVShowRenaming.java'
3. Simply follow the prompts.

Notes on usage:
* The parent directory of the show needs to be the same as the show name. Rename the parent directory to what the show is called before running the program.
* When the program asks for character counts make sure to count every single character, including spaces. Don't worry if you mess up, you'll have a chance to check if it'll come out correct before it is changed.
