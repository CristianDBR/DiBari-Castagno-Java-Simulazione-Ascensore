import java.util.ArrayList;

public class Ascensore {
    private int pianoCorrente;
    private int capienzaMassima;
    private ArrayList<Persona> personeDentro;
    private boolean porteAperte;

    public Ascensore(int capienzaMassima) {
        this.pianoCorrente = 1; // partenza dal piano 1
        this.capienzaMassima = capienzaMassima;
        this.personeDentro = new ArrayList<>();
        this.porteAperte = false;
    }

    public int getPianoCorrente() {
        return pianoCorrente;
    }

    public boolean isPorteAperte() {
        return porteAperte;
    }

    public void apriPorte() {
        if (personeDentro.size() < capienzaMassima) {
            porteAperte = true;
            System.out.println("Porte aperte al piano " + pianoCorrente);
        } else {
            System.out.println("Ascensore pieno, porte non aperte.");
        }
    }

    public void chiudiPorte() {
        porteAperte = false;
        System.out.println("Porte chiuse al piano " + pianoCorrente);
    }

    public void aggiungiPersona(Persona p) {
        if (porteAperte && personeDentro.size() < capienzaMassima) {
            personeDentro.add(p);
            System.out.println(p + " è salita sull'ascensore al piano " + pianoCorrente);
        } else {
            System.out.println("Non è possibile aggiungere " + p.getId() + ": " + (porteAperte ? "Ascensore pieno" : "Porte chiuse"));
        }
    }

    public void rimuoviPersoneArrivate() {
        personeDentro.removeIf(p -> p.getPianoDestinazione() == pianoCorrente && porteAperte);
    }

    public void salita() {
        if (pianoCorrente < 10) {
            pianoCorrente++;
            System.out.println("Ascensore in salita: piano " + pianoCorrente);
        }
    }

    public void discesa() {
        if (pianoCorrente > 1) {
            pianoCorrente--;
            System.out.println("Ascensore in discesa: piano " + pianoCorrente);
        }
    }

    public void decidiDirezione(Piano[] piani) {
        boolean deveSalire = false;
        boolean deveScendere = false;

        for (Persona p : personeDentro) {
            if (p.getPianoDestinazione() > pianoCorrente) {
                deveSalire = true;
            } else if (p.getPianoDestinazione() < pianoCorrente) {
                deveScendere = true;
            }
        }

        if (personeDentro.isEmpty()) {
            for (Piano piano : piani) {
                if (!piano.getCodaPersone().isEmpty()) {
                    if (piano.getNumeroPiano() > pianoCorrente) {
                        deveSalire = true;
                    } else if (piano.getNumeroPiano() < pianoCorrente) {
                        deveScendere = true;
                    }
                }
            }
        }

        if (deveSalire && !deveScendere) {
            salita();
        } else if (deveScendere && !deveSalire) {
            discesa();
        } else if (deveSalire && deveScendere) {
            int sopra = 0, sotto = 0;
            for (Piano piano : piani) {
                if (piano.getNumeroPiano() > pianoCorrente)
                    sopra += piano.getCodaPersone().size();
                else if (piano.getNumeroPiano() < pianoCorrente)
                    sotto += piano.getCodaPersone().size();
            }
            if (sopra >= sotto)
                salita();
            else
                discesa();
        } else {
            System.out.println("Ascensore fermo al piano " + pianoCorrente);
        }
    }

    @Override
    public String toString() {
        return "Ascensore al piano " + pianoCorrente + " con porte " + (porteAperte ? "aperte" : "chiuse") +
                " | Persone a bordo: " + personeDentro.size();
    }
}
