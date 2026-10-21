package ma.emsi.ayatijini.tp0.service;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;
import java.util.Locale;

/**
 * Classe de service pour les modificateurs de question.
 * Un modificateur de question prend en entrée une question et retourne
 * la question modifiée.
 */
@Dependent // Portée CDI pour rendre la classe injectable par CDI dans les autres classes.
// Portée Dependent : l'instance sera supprimée quand la classe qui l'a injectée sera supprimée.
public class GenerateurReponse implements Serializable { // Car CDI peut mettre l'instance en mémoire secondaire.

    /**
     * Voyelles (minuscules, avec accents) comptées par le traitement.
     */
    private static final String VOYELLES = "aeiouyàâäéèêëîïôöùûüœ";

    /**
     * Nombre de mots du rôle système affichés au début de la première réponse.
     */
    private static final int NB_MOTS_ROLE = 4;

    /**
     * Traitement personnel : "miroir" de la question.
     * La réponse contient :
     * <ul>
     *   <li>pour la première question seulement (roleSysteme non null), les premiers mots du rôle système
     *   entre crochets, suivis d'un saut de ligne ;</li>
     *   <li>la question lue à l'envers, lettre par lettre (donc les mots sont aussi dans l'ordre inverse) ;</li>
     *   <li>un saut de ligne puis des statistiques sur la question : nombre de mots, de lettres et de voyelles,
     *   suivies d'un saut de ligne pour séparer cette réponse de la suivante.</li>
     * </ul>
     *
     * @param question    La question à traiter, non vide.
     * @param roleSysteme Le rôle système à utiliser pour la première question ; null ensuite.
     * @return La réponse construite à partir de la question.
     */
    public String genererReponse(String question, String roleSysteme) {
        String texte = question.strip();
        StringBuilder resultat = new StringBuilder();

        if (roleSysteme != null) {
            resultat.append("[Rôle : ").append(debutDuRole(roleSysteme)).append("]\n");
        }

        resultat.append(new StringBuilder(texte).reverse()).append("\n");

        int nbMots = texte.split("\\s+").length;
        long nbLettres = texte.chars().filter(Character::isLetter).count();
        long nbVoyelles = texte.toLowerCase(Locale.FRENCH).chars().filter(c -> VOYELLES.indexOf(c) >= 0).count();
        resultat.append("(").append(nbMots).append(" mot(s), ")
                .append(nbLettres).append(" lettre(s), ")
                .append(nbVoyelles).append(" voyelle(s))\n");

        return resultat.toString();
    }

    /**
     * Retourne les premiers mots du rôle système, suivis de "..." si le rôle est plus long.
     *
     * @param roleSysteme Le rôle système.
     * @return Les premiers mots du rôle.
     */
    private String debutDuRole(String roleSysteme) {
        String[] mots = roleSysteme.strip().split("\\s+");
        if (mots.length <= NB_MOTS_ROLE) {
            return String.join(" ", mots);
        }
        return String.join(" ", java.util.Arrays.copyOfRange(mots, 0, NB_MOTS_ROLE)) + "...";
    }
}
