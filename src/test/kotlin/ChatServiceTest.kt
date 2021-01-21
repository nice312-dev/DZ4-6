import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ChatServiceTest {

    @Before
    fun clearAll() {
        ChatService.deleteAll()
    }

    @Test
    fun create() {
        MessageService.create(Message(id = 0L, ownerId = 0L, recipientId = 0L, text = "message"))
        val actual = MessageService.read()[0].chatId

        assertFalse("Ожидалось, что при создании первого сообщения будет создан чат.", actual == 0L)
    }

    @Test
    fun read() {
        MessageService.create(Message(id = 0L, ownerId = 0L, recipientId = 0L, text = "message"))
        val actual = ChatService.read().size

        assertTrue("Ожидалось, что список чатов будет непустым.", actual > 0)
    }

    @Test
    fun delete() {
        MessageService.create(Message(id = 0L, ownerId = 0L, recipientId = 0L, text = "message"))
        val chatId = MessageService.read()[0].chatId
        val expected = ChatService.read().size - 1

        ChatService.delete(chatId)
        val actual = ChatService.read().size

        assertEquals("Ожидалось, что чат будет удалён.", expected, actual)
    }

    @Test
    fun restore() {
        MessageService.create(Message(id = 0L, ownerId = 0L, recipientId = 0L, text = "message"))
        val chatId = MessageService.read()[0].chatId

        val expected = ChatService.read().size
        ChatService.delete(chatId)
        ChatService.restore(chatId)
        val actual = ChatService.read().size

        assertEquals("Ожидалось, что удалённый чат будет восстановлен.", expected, actual)
    }

    @Test
    fun createMessage() {
        MessageService.create(Message(id = 0L, ownerId = 0L, recipientId = 0L, text = "message"))
        val chatId = MessageService.read()[0].chatId

        ChatService.createMessage(chatId = chatId, ownerId = 0L, recipientId = 0L, text = "message_2")
        val expected = 2
        val actual = ChatService.readMessages(chatId, 0, 0).size

        assertEquals("Ожидалось, что в чат будет добавлено ещё одно сообщение.", expected, actual)
    }

    @Test
    fun readMessages() {
        MessageService.create(Message(id = 0L, ownerId = 0L, recipientId = 0L, text = "message"))
        val chatId = MessageService.read()[0].chatId

        val expected = 1
        val actual = ChatService.readMessages(chatId, 0L, 0).size

        assertEquals("Ожидалось, что в чате будет одно сообщение.", expected, actual)
    }

    @Test
    fun getUnreadChatsCount() {
        val firstChat = Chat()
        val firstChatId = ChatService.create(firstChat)
        MessageService.create(Message(id = 0L, chatId = firstChatId, ownerId = 0L, recipientId = 0L, text = "message"))
        MessageService.create(Message(id = 0L, chatId = firstChatId, ownerId = 0L, recipientId = 0L, text = "message_2"))

        val secondChat = Chat()
        val secondChatId = ChatService.create(secondChat)
        MessageService.create(Message(id = 0L, chatId = secondChatId, ownerId = 0L, recipientId = 0L, text = "message_3"))
        MessageService.create(Message(id = 0L, chatId = secondChatId, ownerId = 0L, recipientId = 0L, text = "message_4"))

        val thirdChat = Chat()
        val thirdChatId = ChatService.create(thirdChat)
        MessageService.create(Message(id = 0L, chatId = thirdChatId, ownerId = 0L, recipientId = 0L, text = "message_3"))
        MessageService.create(Message(id = 0L, chatId = thirdChatId, ownerId = 0L, recipientId = 0L, text = "message_4"))
        MessageService.markRead(chatId = thirdChatId, 0L, 10)

        val expected = 2
        val actual = ChatService.getUnreadChatsCount()

        assertEquals("Ожидалось, будет найден два чата с непрочитанными сообщениями.", expected, actual)
    }
}