package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Course::class,
        Topic::class,
        Note::class,
        Assignment::class,
        Exam::class,
        RoutineItem::class,
        SemesterRecord::class,
        CachedSponsor::class,
        StudyPlan::class,
        StudyPlanTask::class,
        FocusSession::class,
        FacultyMember::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun classNotesDao(): ClassNotesDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Non-destructive safe migrations ensuring users updating from earlier versions never lose existing data
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `assignments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `courseName` TEXT NOT NULL, `dueDate` INTEGER NOT NULL, `dueTime` TEXT NOT NULL, `description` TEXT, `reminderDaysBefore` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `exams` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `courseName` TEXT NOT NULL, `examDate` INTEGER NOT NULL, `examTime` TEXT NOT NULL, `description` TEXT, `reminderDaysBefore` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `routine_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `dayOfWeek` INTEGER NOT NULL, `courseName` TEXT NOT NULL, `startTime` TEXT NOT NULL, `endTime` TEXT NOT NULL, `roomNumber` TEXT)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `semester_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `semesterName` TEXT NOT NULL, `gpa` REAL NOT NULL, `credits` REAL NOT NULL, `createdAt` INTEGER NOT NULL)")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `cached_sponsors` (`id` TEXT PRIMARY KEY NOT NULL, `name` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `websiteUrl` TEXT NOT NULL, `startDate` INTEGER NOT NULL, `endDate` INTEGER NOT NULL, `isActive` INTEGER NOT NULL, `cachedAt` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `study_plans` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `subtitle` TEXT NOT NULL, `startDateMillis` INTEGER NOT NULL, `endDateMillis` INTEGER NOT NULL, `targetDays` INTEGER NOT NULL, `colorHex` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `study_plan_tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `planId` INTEGER NOT NULL, `title` TEXT NOT NULL, `dayNumber` INTEGER NOT NULL, `isDone` INTEGER NOT NULL, `notes` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`planId`) REFERENCES `study_plans`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_study_plan_tasks_planId` ON `study_plan_tasks` (`planId`)")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS `focus_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `durationMinutes` INTEGER NOT NULL, `subject` TEXT NOT NULL, `completedAt` INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS `faculty_members` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `designation` TEXT NOT NULL, `department` TEXT NOT NULL, `email` TEXT NOT NULL, `phone` TEXT NOT NULL, `roomNumber` TEXT NOT NULL, `initials` TEXT NOT NULL, `officeHours` TEXT NOT NULL, `createdAt` INTEGER NOT NULL)")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "class_notes_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
