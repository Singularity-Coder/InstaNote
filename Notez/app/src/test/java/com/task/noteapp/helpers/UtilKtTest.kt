package com.task.noteapp.helpers

import com.google.common.truth.Truth.assertThat

import org.junit.Test

class UtilKtTest {

    @Test
    fun `for current time confirm that the intuitive time is now`() {
        assertThat(timeNow.toIntuitiveDateTime().toLowCase()).isEqualTo("now".toLowCase())
    }
}