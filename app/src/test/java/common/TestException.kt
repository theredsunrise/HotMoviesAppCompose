class TestException : Exception("Error") {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if ((other as Exception).message != this.message) return false
        return true
    }

    override fun hashCode(): Int {
        return 31 * javaClass.hashCode() + message.hashCode()
    }
}