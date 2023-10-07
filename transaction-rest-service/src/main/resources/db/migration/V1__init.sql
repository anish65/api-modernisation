-- Table: public.transaction_detail

-- DROP TABLE IF EXISTS public.transaction_detail;

CREATE SEQUENCE transaction_detail_id_seq;

CREATE TABLE IF NOT EXISTS public.transaction_detail
(
    id integer NOT NULL DEFAULT nextval('transaction_detail_id_seq'::regclass),
    reference_no character varying COLLATE pg_catalog."default" NOT NULL,
    account_id character varying COLLATE pg_catalog."default" NOT NULL,
    amount numeric NOT NULL,
    currency character varying COLLATE pg_catalog."default" NOT NULL,
    description character varying COLLATE pg_catalog."default" NOT NULL,
    transaction_type character varying COLLATE pg_catalog."default",
    created_at time with time zone NOT NULL DEFAULT now(),
    transaction_status character varying COLLATE pg_catalog."default" NOT NULL,
    error_description character varying COLLATE pg_catalog."default",
    CONSTRAINT transaction_detail_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.transaction_detail
    OWNER to postgres;

-- Table: public.account_detail

-- DROP TABLE IF EXISTS public.account_detail;

CREATE SEQUENCE account_detail_id_seq;

CREATE TABLE IF NOT EXISTS public.account_detail
(
    id integer NOT NULL DEFAULT nextval('account_detail_id_seq'::regclass),
    cif_id character varying COLLATE pg_catalog."default" NOT NULL,
    account_id character varying COLLATE pg_catalog."default" NOT NULL,
    account_type character varying COLLATE pg_catalog."default" NOT NULL,
    currency character varying COLLATE pg_catalog."default" NOT NULL,
    account_balance numeric NOT NULL,
    created_at time with time zone NOT NULL DEFAULT now(),
    CONSTRAINT account_detail_pkey1 PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.account_detail
    OWNER to postgres;