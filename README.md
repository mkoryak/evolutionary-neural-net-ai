Evolutionary-Neural-Net-Checker-AI
==================================

A neural network based minimax checker AI that is generated using an evolutionary algorithm.

-----
[Demo](http://mkoryak.github.io/code/2014/10/16/evolutionary-neural-net-checker-ai/) - Play against 3 of the best AIs we evolved.

-----
The Neural Network Checker AI was a semester long final project for a graduate evolutionary computation class I took in 2006.
I worked on a team with Ari Packer to complete this project. Dan Dumont also provided a "hard" checker AI which we used to train against


This project was based on a 2001 publication by Chellapilla K and Fogel DB (2001) "Evolving an Expert Checkers Playing Program without Using Human Expertise".
That paper can be found [here](http://www.natural-selection.com/publications_2001.html)


How The Program Works
=====================
To create an intelligent checkers player, Fogel used an alpha-beta algorithm with a neural network to evaluate the worth of
each leaf.  A population of these strategies (a term Fogel uses to describe a neural network's weights and biases and as
well as a king value K) was then evolved to produce an intelligent board evaluator.  Each strategy's fitness was
calculated by having the strategies compete. The worst performing strategies were then purged from the population and
replaced by mutated copied of the remaining strategies. A ply depth of four, which constitutes two moves per player,
was used when matching the strategies against each other, and a ply depth of eight was used when testing a strategy.
Strategies were tested against human players on the Microsoft Gaming Zone as well as against a version of Chinook which had a reduced difficulty.
