# Quizzes App

Quizzes App is application which communicates with mutiple APIs.

Application contains endpoints which allows user to : 

- [x] Generate quiz with countries from one of the continents : http://localhost:8080/capitolQuiz/{region}/{userId} 

- [x] Get quiz by Id : http://localhost:8080/capitolQuiz/{quizId}

- [x] Get sorted quizzes by score : http://localhost:8080/capitolQuiz/sortedByScore/{order}

- [x] Generate competition quiz (Competition quiz can be generated only once per month) : http://localhost:8080/competitionQuiz/{userId}

- [x] Get score table (contains only competition quizzes) : http://localhost:8080/showCompetitionTable

- [x] Get the most distant capital from quiz (distance from your localization which was found by your IP) : http://localhost:8080/details/theMostDistantCapital/{quizId}

- [x] Get distant from two given towns  http://localhost:8080/details/distanceBetweenTwoTowns/{town1}/{town2}



