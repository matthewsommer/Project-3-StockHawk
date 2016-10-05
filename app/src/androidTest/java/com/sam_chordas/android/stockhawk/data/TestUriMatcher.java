package com.sam_chordas.android.stockhawk.data;

import android.net.Uri;
import android.test.AndroidTestCase;

public class TestUriMatcher extends AndroidTestCase {

    private static final Uri TEST_QUOTE_DIR = Contract.QuoteEntry.CONTENT_URI;
    private static final Uri TEST_QUOTE_WITH_ID = Contract.QuoteEntry.buildUri(12345);

    public void testUriMatcher() {
        UriMatcher testMatcher = new UriMatcher();
        assertEquals(testMatcher.matchUri(TEST_QUOTE_DIR), ProviderUriEnum.QUOTE);
        assertEquals(testMatcher.matchUri(TEST_QUOTE_WITH_ID), ProviderUriEnum.QUOTE_WITH_ID);
    }
}