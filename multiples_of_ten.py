"""Script to generate and work with multiples of 10.
"""

from typing import List


def get_multiples_of_10(count: int = 10) -> List[int]:
    """Return the first `count` positive multiples of 10.
    
    Example:
        >>> get_multiples_of_10(5)
        [10, 20, 30, 40, 50]
    """
    if count <= 0:
        return []
    return [i * 10 for i in range(1, count + 1)]


def get_multiples_up_to(limit: int) -> List[int]:
    """Return all positive multiples of 10 up to `limit` (inclusive).
    
    Example:
        >>> get_multiples_up_to(55)
        [10, 20, 30, 40, 50]
    """
    if limit < 10:
        return []
    return list(range(10, limit + 1, 10))


def is_multiple_of_10(number: int) -> bool:
    """Check if a number is a multiple of 10."""
    return number % 10 == 0


def main():
    print("=== Multiples of 10 Generator ===")
    
    try:
        user_input = input("How many multiples of 10 would you like? (default: 10): ").strip()
        count = int(user_input) if user_input else 10
    except ValueError:
        print("Invalid input. Defaulting to 10.")
        count = 10

    multiples = get_multiples_of_10(count)
    print(f"\nThe first {count} multiples of 10 are:")
    print(multiples)


if __name__ == "__main__":
    main()
