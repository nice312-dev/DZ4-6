interface CrudService<E> {
    fun create(entity: E): Long
    fun read(): List<E>
    fun update(entity: E)
    fun delete(id: Long)
    fun restore(id: Long)
    fun markRead(id: Long)
}