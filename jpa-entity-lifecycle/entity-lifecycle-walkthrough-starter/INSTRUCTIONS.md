# Exercise: ID Strategies and Lifecycle Transitions

## What You'll Build

Two entities — Customer (IDENTITY) and Order (SEQUENCE) — with the
right annotations, then watch a pre-written test walk them through
every lifecycle state.

## Requirements

- Customer uses @GeneratedValue(strategy = IDENTITY)
- Order uses @GeneratedValue(strategy = SEQUENCE) plus
  @SequenceGenerator
- Run the provided LifecycleTest and confirm SQL logs show:
  - IDENTITY INSERT fires on persist
  - SEQUENCE INSERT fires only at flush

## Starter Code

- H2 database (no Postgres needed)
- Customer and Order classes — fields complete, only @Id and
  @GeneratedValue have TODOs
- LifecycleTest is fully written, walks both entities through
  persist → detach → merge → remove

## Verification

- LifecycleTest passes
- SQL log shows the timing difference between IDENTITY and SEQUENCE
- You can explain why SEQUENCE allows JDBC batching but IDENTITY doesn't

## Common Mistakes

- Forgetting @SequenceGenerator
- Calling persist on a managed entity (silent no-op)
- Mixing up detach (local) and remove (emits DELETE)