# Escaperoom-Serverapplicatie

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Websocket Message Broker](#websocket-message-broker)

## General info
Dit project is onderdeel van de escape room van Stichting Jeugdcarrousel ('t Stokperdje).
Dit project is een centraal punt, van waaruit alle overige escape room componenten worden aangestuurd en uitgelezen.
	
## Technologies
Project is created with:
* Java JDK: 11
* Spring Boot Framework: 2.1.9
	
## Setup
To run this project, build image and run it with Docker using:

```
$ docker-compose build
$ docker-compose up -d
```

## Websocket Message Broker
Dit project bevat een simpele Websocket Message Broker.
Het websocket endpoint is beschikbaar via **ws://{ip}:8081/escaperoom**

### Broker Topics 
Na een bepaalde gebeurtenis in de escape room verzend de broker
een message naar alle geabonneerde clients d.m.v.
onderstaande topics.
<table>
    <tr>
        <th>Onderwerp</th>
        <th>Topic</th>
        <th>What's in it</th>
    </tr>
    <tr>
        <td>Berichten</td>
        <td>/topic/berichten</td>
        <td></td>
    </tr>
    <tr>
        <td>Sessie</td>
        <td>/topic/sessie</td>
        <td></td>
    </tr>
    <tr>
        <td>Tijd</td>
        <td>/topic/tijd</td>
        <td></td>
    </tr>
    <tr>
        <td>Buit</td>
        <td>/topic/buit</td>
        <td></td>
    </tr>
    <tr>
        <td>Pinslot</td>
        <td>/topic/pinslot</td>
        <td></td>
    </tr>
</table>