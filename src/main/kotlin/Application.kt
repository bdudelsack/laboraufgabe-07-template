import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import java.util.*

data class Book(val uuid: String, val author: String, val title: String) {
    constructor(author: String, title: String) : this(UUID.randomUUID().toString(), author, title)
}

class BookService {
    val storage: MutableMap<String, Book> = mutableMapOf()

    fun getAll(): List<Book> = storage.values.toList();
    fun getByUUID(uuid: String): Book? = storage.get(uuid);
    fun deleteByUUID(uuid: String): Book? = storage.remove(uuid)

    fun insert(author: String, title: String): Book {
        val book = Book(author, title)
        storage[book.uuid] = book;
        return book
    }

    fun updateByUUID(uuid: String, author: String? = null, title: String? = null) {
        storage[uuid]?.let { book ->
            storage[uuid] = book.copy(author = author ?: book.author, title = title ?: book.title)
        }
    }
}

fun Application.module() {
    TODO("Configure ContentNegotiation")

    di {
        bind() from singleton { TODO("Initiate BookService") }
    }

    routing {
        val bookService: BookService by di().instance()

        TODO("Implement API methods")
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, watchPaths = listOf("build"), module = Application::module).apply {
        start(wait = true)
    }
}