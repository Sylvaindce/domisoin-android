package com.sylvain.domisoin.Utilities;

/**
 * Created by sylvain on 15/01/18.
 */

public class ManageErrorText {

    public static String manage_my_error(String error) {
        error = error.replace("{", "");
        error = error.replace("}", "");
        error = error.replace("detail", "");
        error = error.replace(":", "");
        error = error.replace("'", "");
        error = error.replace("\"", "");
        error = error.replace("does not exist", "n'existe pas");
        error = error.replace("wrong password or email.", "Mauvais email ou mot de passe");
        error = error.replace("user with this email already exists", "Utilisateur avec ce courriel existe déjà");
        error = error.replace("[", " ");
        error = error.replace("]", "");
        error = error.replace("\n", "");
        error = error.replace("enter a valid email address", "Entrez une adresse mail valide");
        error = error.replace("Not found", "Non trouvé");
        error = error.replace("User Is Not Confirmed", "Compte utilisateur en attente de validation");
        error = error.replace("message", "");
        return error;
    }

}
