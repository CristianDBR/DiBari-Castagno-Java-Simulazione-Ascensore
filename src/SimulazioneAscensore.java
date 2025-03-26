public class SimulazioneAscensore {
    public static void main(String[] args) {
        final int NUMERO_PIANI = 10;
        final int DURATA_SIMULAZIONE = 100;  // secondi
        final int CAPACITA_ASCENSORE = 5;
        int idContatore = 1;  // identificativo per le persone

        // Creazione dei piani
        Piano[] piani = new Piano[NUMERO_PIANI];
        for (int i = 0; i < NUMERO_PIANI; i++) {
            piani[i] = new Piano(i + 1);
        }

        // Creazione dell'ascensore
        Ascensore ascensore = new Ascensore(CAPACITA_ASCENSORE);

        // Simulazione per DURATA_SIMULAZIONE secondi
        for (int secondo = 1; secondo <= DURATA_SIMULAZIONE; secondo++) {
            System.out.println("\nSecondo " + secondo);

            // Genera una nuova persona in un piano casuale (da 1 a 10)
            int pianoPartenza = (int) (Math.random() * NUMERO_PIANI) + 1;
            Persona nuovaPersona = new Persona(idContatore++, pianoPartenza);
            piani[pianoPartenza - 1].aggiungiPersonaCoda(nuovaPersona);

            // Simula il movimento dell'ascensore
            // 1. Apri le porte
            ascensore.apriPorte();

            // 2. Se ci sono persone in coda sul piano corrente, farle salire
            Piano pianoCorrente = piani[ascensore.getPianoCorrente() - 1];
            while (!pianoCorrente.getCodaPersone().isEmpty() && ascensore.isPorteAperte() && ascensore.toString().contains("Persone a bordo:") == false) {
                Persona p = pianoCorrente.rimuoviPersonaCoda();
                if (p != null) {
                    ascensore.aggiungiPersona(p);
                }
            }

            // 3. Far scendere le persone che hanno raggiunto la destinazione
            ascensore.rimuoviPersoneArrivate();

            // Stampa lo stato dell'ascensore e del piano corrente
            System.out.println(ascensore);
            System.out.println(pianoCorrente);

            // 4. Chiudi le porte prima di muovere l'ascensore
            ascensore.chiudiPorte();

            // 5. L'ascensore decide la direzione in base alle richieste
            ascensore.decidiDirezione(piani);

            // Simula l'attesa di un secondo (in un'applicazione reale si userebbe Thread.sleep)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Simulazione terminata.");
    }
}
