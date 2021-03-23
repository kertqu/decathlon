# decathlon

Kümnevõistluse punktide arvutamise veebiteenus

Käivitamiseks kasuta 'gradle run'

Kasutatava pordi muutmiseks java system property 'port', näiteks *-Dport=9995* vt gradle.build *application* sektsioon.

Info API kohta http://127.0.0.1:9998/decathlon/application.wadl

API URL:

http://127.0.0.1:9995/decathlon/points/{sex}/{event}/{score}

	sex = men|women
	event = 1...10
	 `   	1 -	100 m jooks
	    	2 -	Kaugushüpe
	    	3 -	Kuulitõuge
	    	4 -	Kõrgushüpe
	    	5 - 400 m jooks
	    	6 -	110 m tõkkejooks
	    	7 -	Kettaheide
	    	8 -	Teivashüpe
	    	9 -	Odavise
	    	10 -	1500 m jooks

	score = ala tulemus, ajad millisekundites, pikkused millimeetrites 

lisaparameeter

	timing = automatic|manual, kui puudub, siis automatic 

tulemuseks JSON väljadega

	  points - punktid
	  event - ala nimi
	  timing, sex, score - sisendväärtused
vea korral

	code - veakood
      	5 - süsteemiviga
      	10 - vigase formaadiga sisendandmed
      	20 - tulemus väljub raamidest
    
  	status - veatekst
  
Näide:
http://127.0.0.1:9995/decathlon/points/men/1/10600?timing=manual

vastus:

	{"score":10600,"timing":"manual","sex":"men","event":"100 m","points":897}

http://127.0.0.1:9995/decathlon/points/men/1/10600?timing=xx
vastus:

	{"code":10,"status":"Illegal value for timing: xx"}

# decathlon.ui

Kümnevõistluse punktide arvutamise veebiteenuse kasutajaliides

Käivitamiseks kasuta 'gradle run'

Serveri aadressi muutmiseks menüü **Valikud->Server**

Pikkused meetrites, näiteks:

	  Kõrgushüpe 2.03
	  Kaugushüpe 7,56
Ajad minutites ja sekundites, näiteks:

	  100m 10.60
	  1500m 4:23.20
