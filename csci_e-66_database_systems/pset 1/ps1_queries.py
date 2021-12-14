#
# CSCI E-66: Problem Set 1, SQL Programming Problems
#

#
# For each problem, use a text editor to add the appropriate SQL
# command between the triple quotes provided for that problem's variable.
#
# For example, here is how you would include a query that finds the
# names and years of all movies in the database with an R rating:
#
sample = """
         SELECT name, year
         FROM Movie
         WHERE rating = 'R';
         """

#
# Problem 5. Put your SQL command between the triple quotes found below.
#
problem5 = """
        SELECT name, year 
        FROM Movie 
        WHERE name 
        LIKE "%Story%";
           """

#
# Problem 6. Put your SQL command between the triple quotes found below.
#
problem6 = """
        SELECT COUNT (*)
        FROM Person, Actor
        WHERE pob LIKE "%South Korea" AND actor_id = id;
           """

#
# Problem 7. Put your SQL command between the triple quotes found below.
#
problem7 = """
        SELECT Oscar.year, Oscar.type, Movie.name
        FROM Person, Oscar, Movie
        WHERE Oscar.person_id = Person.id 
            AND Person.name = 'Meryl Streep' AND Movie.id = Oscar.movie_id;
           """

#
# Problem 8. Put your SQL command between the triple quotes found below.
#
problem8 = """
        SELECT name, year
        FROM Movie
        WHERE name = 'Good Will Hunting' 
            OR name = 'Mystic River';
           """

#
# Problem 9. Put your SQL command between the triple quotes found below.
#
problem9 = """
        SELECT name, year
        FROM Movie
        WHERE rating = 'R'
            AND year = (
                SELECT MIN(year)
		        FROM Movie
		        WHERE rating = 'R');
           """

#
# Problem 10. Put your SQL command between the triple quotes found below.
#
problem10 = """
        SELECT rating, COUNT(*)
        FROM Movie
        WHERE earnings_rank IS NOT NULL
        GROUP BY rating
        ORDER BY COUNT(*) DESC;
            """

#
# Problem 11. Put your SQL command between the triple quotes found below.
#
problem11 = """
        SELECT pob, COUNT(*)
        FROM Person
        WHERE pob IS NOT NULL
        GROUP BY pob
        HAVING COUNT(*) >= 50
        ORDER BY COUNT(*) DESC;
            """

#
# Problem 12. Put your SQL command between the triple quotes found below.
#
problem12 = """
        SELECT COUNT(*)
        FROM (
            SELECT (Person.name)
            FROM Person, Actor
            WHERE Person.id = Actor.actor_id 
            EXCEPT
            SELECT Person.name
            FROM Person, Actor, Movie
            WHERE Person.id = Actor.actor_id 
                AND Movie.id = Actor.movie_id AND Movie.year >= 2010);
            """
    # Alternatively: 
    # SELECT COUNT(DISTINCT id)
    # FROM Person, Actor
    # WHERE Person.id = Actor.actor_id AND actor_id NOT IN(
    #     SELECT actor_id
    #     FROM Actor, Movie
    #     WHERE Movie.id=Actor.movie_id AND Movie.year >= 2010)

#
# Problem 13. Put your SQL command between the triple quotes found below.
#
problem13 = """
        SELECT name, COUNT(Oscar.movie_id)
        FROM Person LEFT OUTER JOIN Oscar ON Person.id = Oscar.person_id
        WHERE pob LIKE '%Spain'
        GROUP BY name;
            """

#
# Problem 14. Put your SQL command between the triple quotes found below.
#
problem14 = """
        SELECT type, AVG(runtime)
        FROM Oscar, Movie
        WHERE Oscar.movie_id = Movie.id
        GROUP BY type;
            """

#
# Problem 15. Put your SQL command between the triple quotes found below.
#
problem15 = """
        SELECT name, runtime
        FROM Movie
        WHERE runtime = (
            SELECT MAX(runtime)
            FROM Movie) OR
            runtime = (
            SELECT MIN(runtime)
            FROM Movie);
            """

#
# Problem 16. Put your SQL command between the triple quotes found below.
#
problem16 = """
        UPDATE Movie
        SET rating =  'PG-13'
        WHERE name = 'Indiana Jones and the Temple of Doom';
            
            """

#
# Problem 17 (required for grad-credit students; optional for others). 
# Put your SQL command between the triple quotes found below.
#
problem17 = """
            SELECT DISTINCT COUNT(actor_id)
            FROM (
                SELECT actor_id, director_id, COUNT (Actor.movie_id)
                FROM Actor, Director
                WHERE Actor.movie_id = Director.movie_id
                GROUP BY actor_id, director_id
                HAVING COUNT (Actor.movie_id) >= 2);
            """

#
# Problem 18 (required for grad-credit students; optional for others). 
# Put your SQL command between the triple quotes found below.
#
problem18 = """
        SELECT Movie.name, Oscar.type
        FROM Movie, Director, Person LEFT OUTER JOIN Oscar ON Movie.id = Oscar.movie_id
        WHERE Movie.id = Director.movie_id 
            AND Director.director_id = Person.id AND Person.name = "Steven Spielberg";
            
            """
