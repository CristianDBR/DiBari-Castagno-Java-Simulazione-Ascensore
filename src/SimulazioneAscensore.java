public class SimulazioneAscensore {
    public static void main(String[] args) {
        final int NUMERO_PIANI = 10;
        final int DURATA_SIMULAZIONE = 100;
        final int CAPACITA_ASCENSORE = 5;
        int idContatore = 1;

        final String RESET  = "\033[0m";
        final String CYAN   = "\033[36m";
        final String YELLOW = "\033[33m";
        final String ORANGE = "\033[38;5;208m";

        Piano[] piani = new Piano[NUMERO_PIANI];
        for (int i = 0; i < NUMERO_PIANI; i++) {
            piani[i] = new Piano(i + 1);
        }

        Ascensore ascensore = new Ascensore(CAPACITA_ASCENSORE);

        for (int ciclo = 1; ciclo <= DURATA_SIMULAZIONE; ciclo++) {
            System.out.println(CYAN + "\n===== CICLO " + ciclo + " =====" + RESET);
            
            // Spawn: ogni 2 cicli, probabilità 75%
            if (ciclo % 2 == 0 && Math.random() < 0.75) {
                int pianoPartenza = (int) (Math.random() * NUMERO_PIANI) + 1;
                Persona nuovaPersona = new Persona(idContatore++, pianoPartenza);
                piani[pianoPartenza - 1].aggiungiPersonaCoda(nuovaPersona);
            }

            for (Piano piano : piani) {
                if (!piano.getCodaPersone().isEmpty()) {
                    if (piano.getNumeroPiano() == ascensore.getPianoCorrente() && ascensore.isPorteAperte()) {
                        piano.resettaTempoAttesa(); 
                    } else {
                        piano.incrementaTempoAttesa();
                    }
                }
            }

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

            if (ascensore.isPorteAperte()) {
                while (ascensore.isPorteAperte() && !piani[ascensore.getPianoCorrente() - 1].getCodaPersone().isEmpty()) {
                    Persona p = piani[ascensore.getPianoCorrente() - 1].rimuoviPersonaCoda();
                    ascensore.aggiungiPersona(p);
                }
            }

            ascensore.rimuoviPersoneArrivate();

            System.out.println(ascensore);
            piani[ascensore.getPianoCorrente() - 1].ascenzore = "";
            System.out.println(piani[ascensore.getPianoCorrente() - 1]);

            if (ascensore.isPorteAperte()) {
                ascensore.chiudiPorte();
            }

            ascensore.decidiDirezione(piani);
            for (Piano p : piani) {
                p.ascenzore = (ascensore.getPianoCorrente() == p.getNumeroPiano()) ? ORANGE + "\t\t[]" : RESET + "\t\t.";
                System.out.println(p);
            }
            System.out.println(CYAN + "===== FINE CICLO " + ciclo + " =====" + RESET);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(CYAN + "Simulazione terminata." + RESET);
    
    }
    
}
