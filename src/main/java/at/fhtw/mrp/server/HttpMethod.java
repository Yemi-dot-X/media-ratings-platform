package at.fhtw.mrp.server;
//Die erlaubten HTTP-Methoden. Ein Wert pro Methode, kein String-Vergleich mehr.

public enum HttpMethod {

    GET,
    POST,
    PUT,
    DELETE;

    //Gibt null zurück, wenn die Methode nicht unterstützt wird (zum Beispiel PATCH).
    public static HttpMethod from(String value) {
        for (HttpMethod method : values()) {
            if (method.name().equalsIgnoreCase(value)) {
                return method;
            }
        }
        return null;
    }
}
