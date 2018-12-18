# Alkalmazások fejlesztése beadandó feladat

Készítette: Bagoly Kunigunda, Dorogi Benjamin, Fenyvesi Attila

## Project management tool

### Funkcionális követelmények

A programba kétféle adattípust lehet rögzíteni:
 - feladatokat
 - humán erőforrásokat (dolgozókat)
 
A feladatok részei:
 - A feladatok részfeladatokból állnak.
 - A részfeladatoknak megadható:
   - mik az előfeltételei (más részfeladatok)
   - melyekkel végezhető párhuzamosan
   - mik az erőforrás igényei
 
A dolgozók adatai:
 - személyes adatok (név, cím, beosztás, fizetés, stb.)
 - képességek egy 5-ös skálán (pl.: PHP: 5/5; Java: 3/5; stb.)
 
Egy munkamenet létrehozása után a program megadja, hogy mely dolgozóknak mely részfeladatokon kell dolgozniuk a leghatékonyabb munkavégzés érdekében. A programba a dolgozók a saját azonosítójukkal be tudnak lépni, és az általuk elvégzett részfeladatokat jóvá tudják hagyni, így nyomon követhető a project végrehajtásának a menete.
  
### Nemfunkcionális követelmények

A programmal szemben támasztott alapvető elvárás - a várhatóan széleskörű és változatos felhasználásból eredendően - a platformfüggetlenség. A programnak futnia kell a legnépszerűbb platformokon (Windows, Linux, OS X).

### Szakterületi követelmények

A programnak képesnek kell lennie bármely szakterület projectmenedzselési feladatainak ellátására, így az általános információk (pl. személyes adatok) rögzítésén túl az üzemeltető számára lehetővé kell tenni, hogy a szakterületre jellemző speciális adatokat (pl. dolgozók képességei vagy a részfeladatok erőforrás igényei) személyre szabhassa.

### Szerepkörök

Admin:
 * Mindenhez (is) hozzáfér

Group Leader :
 * Project létrehozás
 * Memberek csoportba vétele
 * Skillek felvétele
 * Feladatok memberekhez rendelése
 * Folyamatok ellenőrzése

Group Member :
 * Saját skillek megadása
 * Feladat elvégzése (log : idő, kész vagy sem)
 * Folyamat megtekintése

### Adatbázis :
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

### Endpointok
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
   
