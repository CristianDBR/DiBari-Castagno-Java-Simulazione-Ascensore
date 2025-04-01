public class SimulazioneAscensore {
    public static void main(String[] args) {
        final int NUMERO_PIANI = 10;
        final int DURATA_SIMULAZIONE = 100;
        final int CAPACITA_ASCENSORE = 5;
        int idContatore = 1;

        // Colori ANSI per il terminale
        final String RESET  = "\033[0m";
        final String CYAN   = "\033[36m";
        final String YELLOW = "\033[33m";
        final String ORANGE = "\033[38;5;208m";

        // Creazione dei piani
        Piano[] piani = new Piano[NUMERO_PIANI];
        for (int i = 0; i < NUMERO_PIANI; i++) {
            piani[i] = new Piano(i + 1);
        }

        Ascensore ascensore = new Ascensore(CAPACITA_ASCENSORE);

        // Ciclo di simulazione
        for (int ciclo = 1; ciclo <= DURATA_SIMULAZIONE; ciclo++) {
            System.out.println(CYAN + "\n===== CICLO " + ciclo + " =====" + RESET);
            // Stampa lo stato attuale dell'ascensore e dei piani
            
            // Spawn: ogni 2 cicli, probabilità 75%
            if (ciclo % 2 == 0 && Math.random() < 0.75) {
                int pianoPartenza = (int) (Math.random() * NUMERO_PIANI) + 1;
                Persona nuovaPersona = new Persona(idContatore++, pianoPartenza);
                piani[pianoPartenza - 1].aggiungiPersonaCoda(nuovaPersona);
            }

            // Aggiorna il tempo di attesa per ogni piano:
            // Incrementa se ci sono persone in coda e l'ascensore non è sullo stesso piano con porte aperte.
            for (Piano piano : piani) {
                if (!piano.getCodaPersone().isEmpty()) {
                    if (piano.getNumeroPiano() == ascensore.getPianoCorrente() && ascensore.isPorteAperte()) {
                        piano.resettaTempoAttesa(); 
                    } else {
                        piano.incrementaTempoAttesa();
                    }
                }
            }

            // Determina se il piano corrente ha richieste:
            // Richiesta se vi sono persone in coda o se un passeggero a bordo deve scendere.
            boolean richiestaCorrente = !piani[ascensore.getPianoCorrente() - 1].getCodaPersone().isEmpty();
            for (Persona p : ascensore.getPersoneDentro()) {
                if (p.getPianoDestinazione() == ascensore.getPianoCorrente()) {
                    richiestaCorrente = true;
                    break;
                }
            }
            if (richiestaCorrente) {
                ascensore.apriPorte();
            } else {
                System.out.println(YELLOW + "[ASC] Nessuna richiesta al piano " + ascensore.getPianoCorrente() +
                        ". Porte rimangono chiuse." + RESET);
            }

            // Se le porte sono aperte, gestisci salite (fai salire le persone in coda dal piano corrente)
            if (ascensore.isPorteAperte()) {
                while (ascensore.isPorteAperte() && !piani[ascensore.getPianoCorrente() - 1].getCodaPersone().isEmpty()) {
                    Persona p = piani[ascensore.getPianoCorrente() - 1].rimuoviPersonaCoda();
                    ascensore.aggiungiPersona(p);
                }
            }

            // Gestione discese: fai scendere i passeggeri la cui destinazione è il piano corrente
            ascensore.rimuoviPersoneArrivate();

            // Stampa lo stato corrente
            System.out.println(ascensore);
            piani[ascensore.getPianoCorrente() - 1].ascenzore = "";
            System.out.println(piani[ascensore.getPianoCorrente() - 1]);

            // Se le porte sono aperte, chiudile
            if (ascensore.isPorteAperte()) {
                ascensore.chiudiPorte();
            }

            // L'ascensore decide il prossimo piano da servire, considerando sia le richieste interne che quelle esterne
            ascensore.decidiDirezione(piani);
            for (Piano p : piani) {
                p.ascenzore = (ascensore.getPianoCorrente() == p.getNumeroPiano()) ? ORANGE + "\t\t[]" : RESET + "\t\t.";
                System.out.println(p);
            }
            System.out.println(CYAN + "===== FINE CICLO " + ciclo + " =====" + RESET);
            
            // Simula l'attesa di 1 secondo
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(CYAN + "Simulazione terminata." + RESET);
    
    }
    
}
