# Alkalmazások fejlesztése beadandó feladat dokumentációja

Készítette: Bagoly Kunigunda, Dorogi Benjamin, Fenyvesi Attila

## KÖVETELMÉNYANALÍZIS

### 1.1. Célkitűzés

#### 1.1.1. Projektindító dokumentum

__Ez egy olyan alkalmazás, amellyel egy munkacsoport__ (pl. egy adott cégen belüli dolgozók, hallgatók tanulócsoportja) __egy több részből álló projektet menedzselhetnek, nyilvántarthatják annak részfeladatait.__

Amiután egy felhasználó regisztrál az oldalra, lehetősége van új projektet létrehozni, annak részfeladatokat(task-okat) létrehozni és más felhasználókat rendelhet hozzá a részfeladatokhoz. 

#### 1.1.2. Funkcionális elvárások

A programba kétféle adattípust lehet rögzíteni:
* feladatokat
* humán erőforrásokat (dolgozókat)
 
A feladatok részei:
* A feladatok részfeladatokból állnak.
* A részfeladatoknak megadható:
  * mik az előfeltételei (más részfeladatok)
  * melyekkel végezhető párhuzamosan
  * mik az erőforrás igényei
 
A dolgozók adatai:
* személyes adatok (név, cím, beosztás, fizetés, stb.)
* képességek 
* <del>képességek egy 5-ös skálán (pl.: PHP: 5/5; Java: 3/5; stb.)</del>
 
<del>Egy munkamenet létrehozása után a program megadja, hogy mely dolgozóknak mely részfeladatokon kell dolgozniuk a leghatékonyabb munkavégzés érdekében.</del> A programba a dolgozók a saját azonosítójukkal be tudnak lépni, és az általuk elvégzett részfeladatokat jóvá tudják hagyni, így nyomon követhető a project végrehajtásának a menete.

### 1.1.3. Nem funkcionális elvárások

* Felhasználóbarát, egyszerű, letisztult felület
* Keresési eredmények gyors megjelenítése
* Jelszavas azonosítás, jelszavak biztonságos tárolása
* A projekten belüli feladatokhoz és részfeladatokhoz összefüggési gráf generálása
* Privát képek biztonságos elrejtése a látogatók elől
* A programmal szemben támasztott alapvető elvárás - a várhatóan széleskörű és változatos felhasználásból eredendően - a platformfüggetlenség. A programnak futnia kell a legnépszerűbb platformokon (Windows, Linux, OS X).

### 1.1.4. Szakterületi követelmények

A programnak képesnek kell lennie bármely szakterület projectmenedzselési feladatainak ellátására, így az általános információk (pl. személyes adatok) rögzítésén túl az üzemeltető számára lehetővé kell tenni, hogy a szakterületre jellemző speciális adatokat (pl. dolgozók képességei vagy a részfeladatok erőforrás igényei) személyre szabhassa.

### 1.2. Szakterületi fogalomjegyzék
* __projekt / project__: adott szervezeti környezetben megtervezett és végrehajtott tevékenységsorozat, amely konkrét célokat valósít meg, és a célok eléréséhez erőforrásokat rendel. Időben és térben jól körülhatárolt összetett feladat
* __felhasználó / user__: regisztrált és bejelentkezett látogató, aki létrehozhat új projektet, feladatokat vállalhat egy projekten belül, bejegyezheti a feladataihoz hogy elvégezte őket. Létrehozhatja saját profilját, megadhatja skill-jeit, ez alapján oszthatják be más felhasználók taskokhoz. 
* __képesség / skill__: egy felhasználó megadhatja tulajdonságait, erősségeit, képességeit, ami segíti eligazodni a projektmenedzsereket, hogy az adott felhasználót milyen taskokhoz érdemes beosztani. (pl. C++ programozás, kávéfőzés, számítógépes grafikák készítése).
* __tag / member__: a felhasználó egy szerepköre, más felhasználók által létrehozott projektek esetén.
* __projektvezető / leader__: a felhasználó egy szerepköre, saját projektjei esetén.
 
### 1.3. Use-case modell

#### 1.3.1. Szerepkörök
* __user->leader__: A felhasználó szerepköre, amikor a saját projektjét kezeli. Létrehozhat feladatokat a projekthez, membereket rendelhet a feladatokhoz, törölhet feladatokat/membereket, feladatokat oszthat magának, nyomonkövetheti a projekt állapotát.
* __user->member__: A felhasználó szerepköre, amikor mások által létrehozott projektbe meghívják. Publikus feladatokat vállalhat magára, a saját feladatának módosíthatja az állapotát, nyomonkövetheti a projekt állapotát.
* <del>__admin__: God mode. Minden felhasználó minden adatát módosíthatja.</del>

#### 1.3.2. Use-case modell

![Use-case diagramm](/images/usecase.png)

## TERVEZÉS

### 2.1. Oldaltérkép
* Főoldal
  * Bejelentkezés
  * Regisztráció
* Projektek megtekintése
  * Saját projektek megtekintése
    * Projekt szerkesztése
    * Feladat elkezdése
    * Feladat befejezése
    * Feladat törlése
  * Egyéb projektek megtekintése
    * Feladat elkezdése
    * Feladat befejezése
    * Feladat törlése
* Felhasználók
  * Felhasználók megtekintése
  * Felhasználók szerkesztése
  * Új felhasználó felvétele a projekthez
* Profil
  * Profil megtekintése
  * Profil szerkesztése
  * Skillek felvétele
* Kijelentkezés

### 2.2. Végpontok
 * /users
   * "" (GET) : getAll(Authentication auth) : az összes felhasználó lekérdezése
   * "/new" (POST) :  createUser(MessageWrapper user) : új felhasználó felvétele
   * "/login" (POST) : login(String username) User objektum visszaadása username szerint
   * "/{id}" (GET): getUser(Integer id) : user lekérése id alapján
   * "/{id}" (DELETE): deleteUser(Integer id, Authentication auth) : User törlése
   * "/{id}/edit" (PUT) : editUser(Integer id, MessageWrapper user, Authentication auth) user adatainak módosítása
   * "/{id}/skills" (GET) : getSkillLiset(Integer id) : user skilljeinek lekérése
   * "/{id}/skills/add" (PUT) : addSkill(Integer id, MessageWrapper user, Authentication auth) skill hozzárendelése felhasználóhoz
   * "/{id}/skills/remove" (PUT) : removeSkill(Integer id, MessageWrapper user, Authentication auth) skill elvétele felhasználótól
   * "/{id}/ownedProjects" (GET) : getOwnedProjectList(Integer id) : azon projektek lekérése, ahol a user leader
   * "/{id}/projects" (GET) : getProjectList(Integer id) : azon projektek lekérése, ahol a user member
* /skills
  * "" (GET) : getAll() : az összes skill lekérdezése
  * "/new" (POST) :createSkill(MessageWrapper skill) : skill létrehozása
  * /{id} (GET) : getSkill(Integer id) : skill lekérdezése
  * /delete/{id} (DELETE): deleteSkill(Integer id) skill törlése
  * /edit/{id} (PUT):  put(Integer id,Skill skill)
* /tasks
  * "" (GET) : getAll() : az összes task lekérdezése
  * /{id} (GET) : getSkill(Integer id) : task lekérése id alapján
  * /new (POST): createTask( MessageWrapper task,Authentication auth) : task létrehozása
  * /{id}/delete (DELETE): deleteTask(Integer id, Authentication auth) : task törlése
  * /{id}/assign (PUT) : assignTask(Integer id, Authentication auth, MessageWrapper muser) : task hozzárendelése userhez
  * /{id}/unassign (PUT) : unassignTask #nemműködik, de ki tudnánk javítani fél perc alatt
  * /{id}/complete (PUT) : completeTask(integer id, Authentication auth) : task lezárása (befejezése)
  * /{id}/start (PUT) : startTask(Integer id, Authentication auth) : task elkezdése
* /projects
  * "" (GET) : getAll() : az összes project lekérdezése
  * /new (POST) : createProject(MessageWrapper project) : project létrehozása
  * /{id} (GET) : getProject(Integer id) : project lekérése id alapján
  * /{id} (DELETE) : deleteProject(Integer id, Authentication auth) : project törlése
  * /edit/{id} (PUT) : editProject(Integer id, MessageWrapper project, Authentication auth) : project szerkesztése
  * /{id}/addMember : addMember(Integer id,MessageWrapper user, Authentication auth) : tag hozzáadása a projecthez
  * /{id}/members : getMembers(Integer id, Authentication auth) : project tagjainak lekérése
  * /{id}/removeMember : removeMember(Integer id, MessageWrapper member,Authentication auth) : tag törlése a projectből
 
### 2.4. Adatbázis

Az adatbázis táblái és azok attribútumai:
* Users
   * username
   * password
   * name
   * ownedProjects (OneToMany kapcsolatban a project táblával)
   * projects (ManyToMany kapcsolatban a project táblával)
   * skills (ManyToMany kapcsolatban a skill táblával)
   * assignedTasks (ManyToMany kapcsolatban a task táblával)
 * Task
   * name
   * requiredSkills (ManyToMany kapcsolatban a skill táblával)
   * assignees (ManyToMany kapcsolatban a user táblával)
   * prerequisites (ManyToMany kapcsolatban önmagával)
   * requiredBy (--,,--)
   * complete
   * startTime
   * completionTime
   * completedBy
   * isOpen
   * project (ManyToOne kapcsolatban a project táblával)
 * Project
   * leader (ManyToOne kapcsolatban a user táblával)
   * members (ManyToMany kapcsolatban a user táblával)
   * tasks (oneToMany kapcsolatban a task távlával)
   * deadline
   * name
 * Skills
   * name
   * owners (ManyToMany kapcsolatban a user táblával)
   * requiredBy (ManyToMany kapcsolatban a task táblával)
   
 #### 2.4.1. Adatbmodell

![Database diagram](/images/database.png)

### 3.1. Fejlesztőkörnyezet

#### 3.1.1. Felhasznált eszközök

* [Git](https://git-scm.com/) verziókezelő
* [Node.js](https://nodejs.org/en/) Javascript környezet
* [dagre-d3](https://github.com/dagrejs/dagre-d3) JavaScript library a gráfok kirajzolásához
* [GitHub](http://github.com) a projekt közzétételéhez

## FELHASZNÁLÓI DOKUMENTÁCIÓ

### 4.1. Regisztráció, bejelentkezés

__Az oldal bárminemű használata csak regisztráció után lehetséges.__

__Regisztrálni__ a főoldalon található "Regisztráció" gomb segítségével lehetséges, ezután egy felugró ablakban űrlap segítségével lehet megadni a felhasználó adatait. Ezután a program automatikusan belépteti a felhasználót a regisztrált adataival.

![Registration](/images/registr.png)

__Bejelentkezni__ szintén a főoldalon lehetséges, felhasználónév (username) és jelszó (password) megadásával. Ekkor a felhasználót autentikálja az applikáció, majd megjeleníti a "Projektek" oldalt.

![Login](/images/index.png)

### 4.2. Projektek kezelése

A "Projektek" oldalon a felhasználó megtekintheti az eddigi projektjeit, kiválaszthat egy projektet megtekintésre mások által vezetett projektekből ("__Egyéb projectek__"), szerkesztheti a saját projektjeit vagy a "__Saját projektek__" cím mellett a __+__ ikon segítségével létrehozhat egy új saját projektet. Új projekt létrehozása esetén a felhasználónak meg kell adni a projekt nevét egy felugró ablakban. A további adatait a projektnek (taskok, előfeltételek, felhasználók akik dolgozhatnak a projekten) később tudja hozzáadni, a "Projekt szerkesztése" oldalon.

![Projects](/images/projects.png)

### 4.3. Menüsáv, felhasználó profilja

A felső __menüsáv__ belépés után mindig látható. 

![Menu](/images/menu.png)

A "Projectek" menüpont segítségével a felhasználó visszajuthat a főoldalra úgymond, ahol az összes projektje van felsorolva. A "Profil" menüponttal megnyithatja személyes profilját, ahol megváltoztathatja az adatait és felvehet új skilleket.

![Profile](/images/profile.png)

A menüsávban "Kilépés" menüpontra kattintás után a program értelemszerűen kilépteti a felhasználót és visszadobja a kezdeti Regisztráció/Bejelentkezés oldalra.

### 4.4. Projekt szerkesztése

A felhasználónak lehetősége van __szerkeszteni a projekteket__, a "Projektek" oldalon kell kiválasztania, hogy melyiket. A program ezutántól legenerálja a projekt-részfeladatok-gráfját, ahol ábrázolva van, hogy melyik task milyen másik tasknak az előfeltétele. Amennyiben a felhasználó __kattint egy gráf csúcspontra, avagy taskra__, akkor megtekintheti a task jellemzőit (kik dolgoznak rajta, milyen állapotban van), avagy szerkesztheti a taskot. A "__Megkezd/csatlakozik__" gombbal megkezdheti a taskot, amennyiben az nincs megkezdve, illetve csatlakozhat hozzá, ha már igen. Lehetősége van __törölni a taskot__ a projektből, ha annak nincs ráépülője (semelyik másik tasknak nem előfeltétele) és senki nem kezdte már el. __Új taskot hozzáadni a projekthez__ a leadernek van joga (az a user, aki létrehozta a projektet), ezt a gráf mellett található fehér __+__ gombbal teheti meg.

![ProjectEdit](/images/project.png)
