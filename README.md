# Escaperoom-Serverapplicatie

## Inhoud
* [Algemene informatie](#algemene-informatie)
* [Technologieen](#technologieen)
* [Setup](#setup)
* [Websocket Message Broker](#websocket-message-broker)

## Algemene informatie
Dit project is onderdeel van de escape room van Stichting Jeugdcarrousel ('t Stokperdje).
Dit project is een centraal punt van waaruit alle overige escape room componenten worden aangestuurd en uitgelezen.

Ook is dit project het punt waarin alle informatie rondom een draaiende sessie wordt bijgehouden, denk hierbij aan praktische dingen, zoals de tijd, teamnaam, de gewonnen buit etc.
Al deze dingen worden door middel van een websocket verbinding doorgepushed naar luisterende clients.
	
## Technologieen
Dit project is gebaseerd op:
* Java JDK: 11
* Spring Boot Framework: 2.1.9
* Docker
	
## Setup
Dit project vereist een docker network "escaperoom". Als deze nog niet bestaat, maak deze dan aan met:
```
$ docker network create escaperoom
```
Om dit project te starten, build de image en run het met Docker:

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
        <td>
            show: boolean<br>
            message: string
        </td>
    </tr>
    <tr>
        <td>Sessie</td>
        <td>/topic/sessie</td>
        <td></td>
    </tr>
    <tr>
        <td>Tijd</td>
        <td>/topic/tijd</td>
        <td>
        </td>
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