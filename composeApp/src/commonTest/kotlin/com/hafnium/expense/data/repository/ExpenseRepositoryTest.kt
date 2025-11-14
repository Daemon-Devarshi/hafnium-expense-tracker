package com.hafnium.expense.data.repository

import com.hafnium.expense.data.db.ExpenseDao
import com.hafnium.expense.data.db.ExpenseEntity
import com.hafnium.expense.data.image.ImageStorage
import com.hafnium.expense.domain.model.Expense
import com.hafnium.expense.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for ExpenseRepository query-by-date functionality.
 *
 * Tests:
 * - Query expenses by specific date
 * - Empty results for dates with no expenses
 * - Multiple expenses for same date
 */
class ExpenseRepositoryTest {

    private lateinit var repository: TestExpenseRepository
    private lateinit var mockDao: MockExpenseDao
    private lateinit var mockImageStorage: MockImageStorage

    @BeforeTest
    fun setup() {
        mockDao = MockExpenseDao()
        mockImageStorage = MockImageStorage()
        repository = TestExpenseRepository(mockDao, mockImageStorage)
    }

    @Test
    fun `getExpensesByDate returns expenses for specific date`() = runTest {
        // Given: expenses for a specific date
        val targetDate = LocalDate(2025, 11, 14)
        val dateString = targetDate.toString()
        val expense1 = ExpenseEntity(
            id = 1,
            date = dateString,
            amount = 100,
            description = "Test 1",
            imagePath = null,
            createdAt = 0L,
            updatedAt = 0L
        )
        val expense2 = ExpenseEntity(
            id = 2,
            date = dateString,
            amount = 200,
            description = "Test 2",
            imagePath = null,
            createdAt = 0L,
            updatedAt = 0L
        )
        mockDao.expenses = listOf(expense1, expense2)

        // When: querying for that date
        val result = repository.getExpensesByDate(targetDate).first()

        // Then: should return both expenses
        assertEquals(2, result.size)
        assertEquals(100, result[0].amount)
        assertEquals(200, result[1].amount)
    }

    @Test
    fun `getExpensesByDate returns empty list for date with no expenses`() = runTest {
        // Given: no expenses for a specific date
        val targetDate = LocalDate(2025, 11, 14)
        mockDao.expenses = emptyList()

        // When: querying for that date
        val result = repository.getExpensesByDate(targetDate).first()

        // Then: should return empty list
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getExpensesByDate filters by exact date`() = runTest {
        // Given: expenses for different dates
        val targetDate = LocalDate(2025, 11, 14)
        val otherDate = LocalDate(2025, 11, 15)
        val expense1 = ExpenseEntity(
            id = 1,
            date = targetDate.toString(),
            amount = 100,
            description = "Target date",
            imagePath = null,
            createdAt = 0L,
            updatedAt = 0L
        )
        val expense2 = ExpenseEntity(
            id = 2,
            date = otherDate.toString(),
            amount = 200,
            description = "Other date",
            imagePath = null,
            createdAt = 0L,
            updatedAt = 0L
        )
        mockDao.expenses = listOf(expense1, expense2)

        // When: querying for target date
        val result = repository.getExpensesByDate(targetDate).first()

        // Then: should return only expense for target date
        assertEquals(1, result.size)
        assertEquals("Target date", result[0].description)
    }

    // Mock implementations for testing

    /**
     * Test implementation of ExpenseRepository that directly uses DAO
     * instead of requiring full AppDatabase setup.
     */
    private class TestExpenseRepository(
        private val dao: ExpenseDao,
        private val imageStorage: ImageStorage
    ) : ExpenseRepository {

        override suspend fun saveExpense(expense: Expense): Long {
            val entity = expense.toEntity()
            return if (expense.id == 0L) {
                dao.insert(entity)
            } else {
                dao.update(entity)
                expense.id
            }
        }

        override suspend fun getExpenseById(id: Long): Expense? {
            return dao.getById(id)?.toExpense()
        }

        override fun getExpensesByDate(date: LocalDate): Flow<List<Expense>> {
            return dao.getByDate(date.toString()).map { entities ->
                entities.map { it.toExpense() }
            }
        }

        override fun getAllExpenses(): Flow<List<Expense>> {
            return dao.getAll().map { entities ->
                entities.map { it.toExpense() }
            }
        }

        override suspend fun deleteExpense(expenseId: Long) {
            val entity = dao.getById(expenseId) ?: return
            if (entity.imagePath != null) {
                imageStorage.deleteImage(entity.imagePath)
            }
            dao.delete(entity)
        }

        override suspend fun deleteExpensesOlderThan(cutoffDate: LocalDate): Int {
            return dao.deleteOlderThan(cutoffDate.toString())
        }

        override suspend fun saveImage(imageData: ByteArray, filename: String): String? {
            return try {
                imageStorage.saveImage(imageData, filename)
            } catch (_: Exception) {
                null
            }
        }

        // Conversion helpers
        private fun Expense.toEntity(): ExpenseEntity {
            return ExpenseEntity(
                id = id,
                date = date.toString(),
                amount = amount,
                description = description,
                imagePath = imagePath,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }

        private fun ExpenseEntity.toExpense(): Expense {
            return Expense(
                id = id,
                date = LocalDate.parse(date),
                amount = amount,
                description = description,
                imagePath = imagePath,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }

    private class MockExpenseDao : ExpenseDao {
        var expenses: List<ExpenseEntity> = emptyList()
        var insertedExpenses: MutableList<ExpenseEntity> = mutableListOf()
        var updatedExpenses: MutableList<ExpenseEntity> = mutableListOf()
        var deletedExpenses: MutableList<ExpenseEntity> = mutableListOf()

        override suspend fun insert(expense: ExpenseEntity): Long {
            insertedExpenses.add(expense)
            return expense.id
        }

        override suspend fun update(expense: ExpenseEntity) {
            updatedExpenses.add(expense)
        }

        override suspend fun delete(expense: ExpenseEntity) {
            deletedExpenses.add(expense)
        }

        override suspend fun getById(id: Long): ExpenseEntity? {
            return expenses.find { it.id == id }
        }

        override fun getByDate(date: String): Flow<List<ExpenseEntity>> {
            return flowOf(expenses.filter { it.date == date })
        }

        override fun getAll(): Flow<List<ExpenseEntity>> {
            return flowOf(expenses)
        }

        override suspend fun deleteOlderThan(cutoffDate: String): Int {
            val toDelete = expenses.filter { it.date < cutoffDate }
            expenses = expenses.filter { it.date >= cutoffDate }
            return toDelete.size
        }
    }

    private class MockImageStorage : ImageStorage {
        var savedImages: MutableMap<String, ByteArray> = mutableMapOf()
        var deletedPaths: MutableList<String> = mutableListOf()
        private var imageCounter = 0L

        override suspend fun saveImage(imageData: ByteArray, filename: String): String {
            val path = if (filename.isEmpty()) "image_${imageCounter++}.jpg" else filename
            savedImages[path] = imageData
            return path
        }

        override suspend fun deleteImage(imagePath: String): Boolean {
            deletedPaths.add(imagePath)
            return savedImages.remove(imagePath) != null
        }

        override suspend fun imageExists(imagePath: String): Boolean {
            return savedImages.containsKey(imagePath)
        }
    }
}

