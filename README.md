TIN - Semestrální práce
=======================

Ondřej Macoszek (macosond@fel.cvut.cz), 2011

## Zadání

http://service.felk.cvut.cz/courses/36TI/xkacer/sem7.html

 * mapa s dvěma druhy míst - rizikové a obyčejné
 * cesty mezi městy (s označením délky)
 * ne všechna místa ve městě jsou propojena cestou (rozlišit na polní a pěší cestu)
 * hlídky rozmístěny vždy jedna na každém rizikovém místě
 * pokud je víc problému na jednom místě, pak hlídky, jejichž nejkratší vzdálenost je nižší než předem daná konstanta, se přesunou na rizikové místo

## Vstup
 * int konstanta maximální vzdálenosti k hlídce
 * int r … počet rizikových míst
 * r řádků … p x y … uid místa a souřadnice
 * int o … počet obyčejných míst
 * o řádků … p x y 
 * int c … počet cest
 * c řádků … p1 p2 d … místo, místo, délka cesty
 * int f … počet pěších cest 
 * f řádků … p1 p2 … místo, místo

## Řešení

 * Dopočítat vzdálenost pěších cest jako euklidovskou vzdálenost mezi souřadnicemi
 * Pro každé rizikové místo provést Dijkstrův algoritmus
   * tj. zjistit vzdálenosti k ostatním bodům
   * vybrat nejbližší riziková místa s vzdáleností nižší než zadaná konstanta 
 * Hotovo

 * V kódu hojně používány implementace množiny HashSet (s konstantní operací contains)

## Grafická ukázka vstupních dat
![Ukázka vstupních dat graficky](https://github.com/peruginni/Police-Station-Problem/raw/master/img/data.png)


## Zdroje a literatura
 * Teoretická informatika (Josef Kolář)
 * http://www.algoritmy.net/article/5108/Dijkstruv-algoritmus