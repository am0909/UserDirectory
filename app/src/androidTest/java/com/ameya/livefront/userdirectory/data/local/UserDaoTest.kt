package com.ameya.livefront.userdirectory.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ameya.livefront.userdirectory.data.remote.Coordinates
import com.ameya.livefront.userdirectory.data.remote.Dob
import com.ameya.livefront.userdirectory.data.remote.Location
import com.ameya.livefront.userdirectory.data.remote.Name
import com.ameya.livefront.userdirectory.data.remote.Picture
import com.ameya.livefront.userdirectory.data.remote.Street
import com.ameya.livefront.userdirectory.data.remote.Timezone
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserDaoTest {
    private lateinit var db: UserDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries().build()
        userDao = db.dao
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getUserList() = runTest {
        insertUserList()
        val users = userDao.searchUserList("")
        assertEquals(2, users.size)
        assertEquals(1L, users[0].id)
        assertEquals(2L, users[1].id)
    }

    @Test
    fun searchCompleteUserList() = runTest {
        insertUserList()
        val users = userDao.searchUserList("Jane")
        assertEquals(1, users.size)
        assertEquals("Jane", users[0].name.first)
    }

    @Test
    fun searchPartialUserList() = runTest {
        insertUserList()
        val users = userDao.searchUserList("Ja")
        assertEquals(1, users.size)
        assertEquals("Jane", users[0].name.first)
    }

    @Test
    fun deleteUser() = runTest {
        insertUserList()
        userDao.deleteUserList()
        val users = userDao.searchUserList("")
        assertEquals(0, users.size)
    }

    private suspend fun insertUserList() {
        val userEntity1 = getTestUserEntity(1, first = "John")
        val userEntity2 = getTestUserEntity(2, first = "Jane")
        userDao.insertUserList(listOf(userEntity1, userEntity2))
    }

    private fun getTestUserEntity(
        id: Long,
        first: String,
    ) = UserEntity(
        id = id,
        name = Name(first = first, last = "Doe", title = "Mr"),
        cell = "1234567890",
        dob = Dob(age = 30, date = "1993-01-01"),
        email = "john.doe@email.com",
        gender = "male",
        location = Location(
            city = "New York",
            coordinates = Coordinates(latitude = "47.1430", longitude = "-79.9533"),
            country = "USA",
            postcode = "10001",
            state = "NY",
            street = Street(name = "5th Avenue", number = 123),
            timezone = Timezone(description = "GMT-5", offset = "-05:00")
        ),
        phone = "1234567890",
        picture = Picture(
            large = "large_url", medium = "medium_url",
            thumbnail = "thumbnail_url"
        )
    )
}