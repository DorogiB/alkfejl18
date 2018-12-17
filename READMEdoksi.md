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
 * user/new
   * *createMember(String name,String password,String role,List<Integer> skills) --a skillek id szerint vannak tárolva*
 * user/edit
   * *modifyUser(String newName, String newPassword)*
 * user/addskills
   * *addSkills(List<Integer> skills) --a skillek id szerint vannak tárolva*
 * member/project/tasks
   * *getTasks(User user)*
 * member/project/completeTask
   * *completeTask(Integer duration)*
 * member/project/getStatus
    * *getProjectStatus(Project project)*
 * leader/group/create
   * *createGroup(String groupName,List<User> members)*
 * leader/group/members/add
   * *addGroupMember(Group group,User member)*
 * leader/group/members/remove
   * *removeGroupMember(Group group, User member)*
 * leader/project/new
   * *createProject(Group group,String name)*
   * *createProject(Group group,String name,DateTime deadline)*
 * leader/project/createTask
   * *createTask(Project projectId, List<Integer> requiredSkills,List<Integer> prerequisites) --az előfeltételek id szerint vannak tárolva*
  * leader/project/assigntask
    * *assignTask(Task task,User assignee)*
  * leader/project/calculateoptimal
    * *calcOptimalAssignees(Project project)*
  * leader/project/createSkill
    * *createSkill(String name, Integer code)*
  * leader/project/getStatus
    * *getProjectStatus(Project project)*
 * admin/editusers
   * *editUser(String name, String password, String role, List<Integer> skills)*
 * admin/switchtouserprofile
  * *fogalmam nincs ezt pontosan még hogy fogjuk implementálni*
 
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

### 4.1. Felhasználói dokumentáció

#### 4.1.1. Regisztráció, bejelentkezés

__Az oldal bárminemű használata csak regisztráció után lehetséges.__

__Regisztrálni__ a főoldalon található "Regisztráció" gomb segítségével lehetséges, ezután egy felugró ablakban űrlap segítségével lehet megadni a felhasználó adatait. Ezután a program automatikusan belépteti a felhasználót a regisztrált adataival.

__Bejelentkezni__ szintén a főoldalon lehetséges, felhasználónév (username) és jelszó (password) megadásával. Ekkor a felhasználót autentikálja az applikáció, majd megjeleníti a "Projektek" oldalt.



[Registration](images/registr.png)





