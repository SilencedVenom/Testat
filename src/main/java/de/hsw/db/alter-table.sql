ALTER TABLE "transactions"
    ALTER COLUMN "created_at" TYPE DATE
        USING "created_at"::DATE;
