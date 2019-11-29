from io import open
from os import path

from setuptools import setup, find_packages

here = path.abspath(path.dirname(__file__))

with open(path.join(here, "README.md"), encoding="utf-8") as f:
    long_description = f.read()

setup(
    name="iot-device",
    version="1.0.0",
    description="A sample Pyspark project that reads from Kinesis and Sinks to MySQL",  # Optional
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/eschizoid/spark-playground",
    author="Mariano Gonzalez",
    author_email="eschizoid@hotmail.com",
    classifiers=[
        "Development Status :: 3 - Production/Stable",
        "Intended Audience :: Developers",
        "Topic :: Software Development :: Build Tools",
        "License :: OSI Approved :: MIT License",
        "Programming Language :: Python :: 3.7",
        "Programming Language :: Python :: 3.8",
    ],
    keywords="pyspark kinesis mysql streaming",
    package_dir={"": "src"},
    packages=find_packages(where="src"),
    python_requires=">=3.6, <4",
    install_requires={
        "spark": ["pyspark=2.4.3"],
        "singleton": ["singleton-decorator=1.0.0"],
        "mysql": ["mysql-connector-python=8.0.17"],
    },
    extras_require={
        "dev": ["check-manifest"],
        "test": ["coverage"],
    },
    project_urls={
        "Source": "https://github.com/eschizoid/spark-playground/iot-device",
    },
)
