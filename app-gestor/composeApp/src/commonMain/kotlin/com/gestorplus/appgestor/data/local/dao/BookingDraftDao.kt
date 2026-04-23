package com.gestorplus.appgestor.data.local.dao

import androidx.room.*
import com.gestorplus.appgestor.data.local.entity.BookingDraftEntity

@Dao
interface BookingDraftDao {
    @Query("SELECT * FROM booking_drafts WHERE id = 1")
    suspend fun getDraft(): BookingDraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDraft(draft: BookingDraftEntity)

    @Query("DELETE FROM booking_drafts WHERE id = 1")
    suspend fun deleteDraft()
}
