## PROJECT THEME

Applicazione backend progettata per gestire in modo sicuro ed efficiente le operazioni di una biblioteca.

L'applicazione è sviluppata in **Java** utilizzando il framework **Spring Boot** e si appoggia a un database relazionale **PostgreSQL**.

L'obiettivo di questo progetto è dimostrare le competenze architetturali e di sviluppo backend, tra cui:
* Progettazione del dominio e persistenza dei dati (Hibernate/JPA)
* Relazioni tra entità ed ereditarietà (Strategy Joined/Single Table)
* Sicurezza e Autenticazione (Spring Security e JWT)
* Paginazione, ricerca e query di aggregazione (JPQL)
* Integrazione e parsing di API esterne
* Gestione di logica transazionale (Prestiti e Prenotazioni)

## DOMAIN

Il dominio si concentra su un catalogo flessibile e su un sistema di utenti con permessi differenziati. 

I ruoli (Role) previsti dal sistema sono 3:
* **ADMIN**: Ha il controllo totale sul sistema, inclusa la gestione degli utenti e l'inserimento di nuovi volumi.
* **LIBRARIAN**: Può gestire il catalogo dei libri, aggiornare lo stato delle copie fisiche e recuperare metadati.
* **MEMBER**: L'utente base della biblioteca, abilitato alla consultazione del catalogo paginato, alla richiesta di prestiti e alle prenotazioni.

## ENTITIES

L'intero progetto è strutturato attorno a 8 entità principali relazionate tra loro, gestite tramite JPA e Hibernate per mappare la persistenza dei dati su database:

1. **User**: Gestisce i dati anagrafici (inclusa la data di registrazione assegnata in automatico alla creazione tramite `@CreationTimestamp`), le credenziali di accesso e l'immagine del profilo.
2. **Role**: Rappresenta i ruoli di sicurezza all'interno dell'applicazione (ADMIN, LIBRARIAN, MEMBER). È legata all'entità User tramite una relazione Many-to-Many, gestita a livello di database tramite la tabella di giunzione `users_roles`.
3. **Author**: Rappresenta l'autore dei volumi, legato ai libri tramite una relazione One-to-Many.
4. **Category**: Categoria o genere letterario di appartenenza di ogni volume.
5. **Book (Abstract)**: Classe padre (astratta) che fa da colonna vertebrale al catalogo. Contiene i campi condivisi da ogni tipologia di libro: ISBN, Titolo, Anno di pubblicazione e contatori delle copie.
6. **PrintedBook**: Classe figlia che estende Book. Rappresenta la copia fisica presente in biblioteca e introduce attributi specifici come la collocazione fisica nello scaffale e il numero di pagine.
7. **EBook**: Classe figlia che estende Book. Rappresenta il volume in formato digitale e include dettagli tecnici come il formato del file (EPUB, PDF) e l'URL sicuro per il download.
8. **Loan**: Rappresenta il prestito effettivo di un libro cartaceo. Collega l'utente al volume e traccia la data di inizio, la scadenza prevista e la data di restituzione effettiva.
9. **Reservation**: Gestisce le richieste di prenotazione per i volumi che al momento hanno zero copie disponibili in catalogo.

## CORE BUSINESS LOGIC

Il sistema gestisce il ciclo di vita dei libri in modo transazionale e sicuro, impedendo inconsistenze nel database:
* **Inventario Dinamico**: Alla creazione di un `Loan`, il sistema verifica la disponibilità e decrementa in automatico il contatore `availableCopies` del libro. Alla restituzione, il contatore viene incrementato.
* **Regole di Prenotazione**: Le `Reservation` sono protette da vincoli di business. Un utente non può prenotare un libro se ci sono copie attualmente disponibili (il sistema lo forzerà a effettuare un prestito diretto), garantendo una gestione efficiente delle code.

## REST API, QUERIES & ERROR HANDLING

L'architettura separa i layer di Controller, Service e Repository.

* **Paginazione e Filtri**: Gli endpoint di ricerca non restituiscono semplici liste, ma oggetti `Page`. Le risposte JSON includono metadati completi (`totalElements`, `totalPages`, `last`), ottimizzati per il consumo da parte di framework frontend. È presente il filtering dinamico per cercare titoli specifici ignorando maiuscole e minuscole.
* **Aggregazioni e DTO**: Sono state scritte query JPQL customizzate per estrarre statistiche precise (es. il conteggio totale dei libri raggruppati per categoria), mappando i risultati direttamente su oggetti DTO/Response di risposta per evitare il trasferimento di dati superflui.
* **Error Handling**: I dati in ingresso sono validati rigorosamente (es. annotazioni `@Validated`, gestione dei duplicati ISBN in fase di inserimento o blocchi su prestiti non validi).

## AUTH & SECURITY

L'autenticazione è implementata tramite **JWT**. Le password degli utenti vengono criptate nel database tramite `BCryptPasswordEncoder` e le rotte sensibili sono protette in base al ruolo dell'utente tramite `Method Security` (es. `@PreAuthorize`).

## 3RD PARTY APIs

Il backend integra due servizi di terze parti:

1. **Cloudinary**: Utilizzato per l'upload e la gestione in cloud delle immagini del profilo degli utenti, salvando nel database esclusivamente l'URL sicuro del file restituito dal servizio remoto.
2. **OpenLibrary API**: Un'integrazione nativa per il data fetching. Passando l'ISBN di un libro a un endpoint dedicato, il sistema si collega al database open-source, effettua il parsing del JSON in tempo reale tramite `ObjectMapper` e restituisce i metadati esatti (Titolo, Anno, Pagine, Autore) per pre-compilare i form di inserimento.

---

## HOW TO

Istruzioni per configurare e testare il progetto in locale.

**1. Prerequisiti**
* Java JDK 25.
* PostgreSQL installato (creare un database vuoto tramite pgAdmin).
* Postman per testare le chiamate.

**2. Variabili d'Ambiente**
Prima di avviare l'applicazione, configura il file `application.yml` nella cartella `src/main/resources`.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nome_del_tuo_db
    username: tuo_username
    password: tua_password
  jpa:
    hibernate:
      ddl-auto: update

# Configurazione JWT
jwt:
  secret: INSERISCI_SECRET_KEY
  expiration: 86400000

# Configurazione Cloudinary
cloudinary:
  cloud_name: tuo_cloud_name
  api_key: tua_api_key
  api_secret: tua_api_secret
```

**3. Postman Collection**
All'interno della repository, nella cartella delle risorse, troverai il file contenente la **Postman Collection** esportata.
* Importa la collection su Postman.
* Ogni richiesta contiene già un body di esempio formattato correttamente.
* **Importante:** Per testare le rotte protette, esegui prima la chiamata di Login (o Registrazione), copia il token restituito nel JSON di risposta e incollalo nei values della key JWT_TOKEN nella sezione Variables della collection.

**4. Avvio**
Avvia il server tramite la classe principale dell'applicazione (entry point `BeExamApplication`).
