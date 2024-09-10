Development notes:


It's funny that I get a task like this, because I've already done something very similar to this myself, but at a much larger scale.
Rather than creating a game myself, I used someone else's game, and exploited the hell out of it, getting the answer way faster than anyone else.
The project is available here:
https://github.com/Vazalemma/PR2_Projects/tree/main/PR2%20Projects
(You may ask more about this as well if it interests you)


So initially I really wanted to create a UI for this task as well, using Angular (as was suggested).
But my biggest weakness has always been setting things up and integrating things together.
After spending 2-3 hours going through guides, YouTube video tutorials, and testing the things I downloaded,
I managed to get Angular to work on localhost:8080, but I couldn't figure out how to integrate it with Java.
Not wanting to spend too much time trying to create a UI, I decided to stick with the console app instead.
(I made an MS Paint prototype for what I wanted the UI to look like)

During development, I decided to put more effort into making everything look nice and logical, because I wouldn't get extra points due to lack of UI.
I created a Cocktail object, the Application has all the game logic, and Controller has all the math and settings.
I ended up only using "random.php" because I didn't see any use in anything else.
Not gonna lie, would rather have the whole list than be given random ones through the website, I don't want to send too many requests too quickly.
Regardless, everything does work, and there should be no bugs.
Also, the tests assure the basic functions of the game are correct.