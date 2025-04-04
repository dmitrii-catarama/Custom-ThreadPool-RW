    Prima metoda de "WriterPreferred" am implementat-o cu semafoare. 
Folosirea semafoarelor in aceasta implementare face ca codul sa para mai
complex, mai complicat, dar permit extinderea mai usoara a codului la
necesitate, in comparatie cu folosirea de wait() sau notifyAll().

    A doua metoda de "WriterPreferred" am implementat-o cu ajutorul blocurilor
synchronized(), wait(), notify() si notifyAll(). In cazul acestui agoritm se
observa ca codul a devenit mai clar, mai usor de parcurs, dar daca vom incerca
sa extindem acest cod cu functionalitati noi, o sa putem pierde din avantaje
mentionate si performanta din cauza folosirii de prea multe notify() si
notifyAll().

    In cazul primei metode, daca se fac foarte multe taskuri si numarul de
thread-uri e mare, semafoarele sunt mai eficiente, caci ele conduc cate un
thread la executie, iar folosirea de notifyAll() ar ocupa mai mult timp
ca sa notifice toti participantii(thread-uri). Dar daca numarul de thread-uri
ar fi mai mic, indiferent de numarul de task-uri, notifyAll() ar fi mai rapid
decat folosirea de sem.acquire() sau sem.release() caci ele reprezinta metode
repetitive si in cazul dat ar ceda dupa performanta.

    In cazul de prioritizare cititori observam fenomenul de "writers starvation"
iar in cazul opus de prioritizare - "readers starvation". In ambele aceste
cazuri se observa lipsa de echilibru, iar in acest sens noi am putea sa
implementam oferirea de prioritate pentru cititori/scriitori dupa un anumit
numar de scrieri/citiri, sau, sa facem schimbam prioritatea in dependenta de
cat timp asteapta scriitorii/cititorii.

Modul de testare:
    Se observa ca se folosesc scrieri/citiri de diferite lungimi, ceea ce ofera
o imagine mai clara asupra la ceea ce se intampla in scenarii de incarcare
diferita.
    Pentru diferite tipuri de ratii de cititori/scriitori si preoritizari cu un
numar mare de taskuri, se poate cu usurinta observa corectitudinea
implementarii pentru scenarii diferite de preoritizari.
    Latenta adaugata la scriere si citire este o buna metoda de a oferi realism
in verificarea implementarii propuse de studenti.
    De asemenea, timpul limitat de executie pentru task-uri a impus reguli
stricte la implementare, astfel pentru ca sa fie incadrata in timp
implementarea, a fost nevoie de formarea unui algoritm de paralelizare pentru
fiecare index a bazei de date, ca sa fie cat mai eficient.
