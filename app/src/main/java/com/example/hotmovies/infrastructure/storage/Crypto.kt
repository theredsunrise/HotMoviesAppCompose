package com.example.hotmovies.infrastructure.storage

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.InvalidParameterException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

interface CryptoInterface {
    fun encrypt(plainBytes: ByteArray): ByteArray
    fun decrypt(encryptedBytes: ByteArray): ByteArray
}

class Crypto : CryptoInterface {
    companion object {
        const val KEYSTORE_ALIAS = "HotMovies"
        const val KEYSTORE = "AndroidKeyStore"
        private const val GCM_IV_LENGTH = 12
        private const val GCM_AUTH_TAG_LENGTH = 128
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

    private val keyStore = KeyStore.getInstance(KEYSTORE).apply { load(null) }
    private val cipher = Cipher.getInstance(TRANSFORMATION)

    private fun createAESKey(alias: String): SecretKey {
        val keyGenerator =
            KeyGenerator.getInstance(ALGORITHM, KEYSTORE)
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(BLOCK_MODE)
            setEncryptionPaddings(PADDING)
            setKeySize(256)
            setUserAuthenticationRequired(false)
        }.build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    private val secretKey: SecretKey by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val secretKeyEntry = keyStore
            .getEntry(KEYSTORE_ALIAS, null) as? KeyStore.SecretKeyEntry
        secretKeyEntry?.secretKey ?: createAESKey(KEYSTORE_ALIAS)
    }

    @Synchronized
    override fun encrypt(plainBytes: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plainBytes)
        return iv + encrypted
    }

    @Synchronized
    override fun decrypt(encryptedBytes: ByteArray): ByteArray {
        if (encryptedBytes.size < GCM_IV_LENGTH) {
            throw InvalidParameterException("Encrypted string has invalid length!")
        }
        val iv = encryptedBytes.copyOfRange(0, GCM_IV_LENGTH)
        val data = encryptedBytes.copyOfRange(GCM_IV_LENGTH, encryptedBytes.size)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_AUTH_TAG_LENGTH, iv))
        return cipher.doFinal(data)
    }
}