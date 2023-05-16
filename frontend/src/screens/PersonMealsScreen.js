import React, { useState, useEffect } from "react";
import { ListGroup, Card, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import Meal from "../components/Meal";
import axios from "axios";
import { useParams } from "react-router-dom";

const PersonMealsScreen = () => {
  const [mealsOBJ, setMealsOBJ] = useState({});

  const { id } = useParams();

  useEffect(() => {
    const fetchMeals = async () => {
      const { data } = await axios.get(`/api/person/${id}`);

      setMealsOBJ(data);
    };

    fetchMeals();
  }, []);

  return (
    <>
      <Link className="btn btn-primary my-3" to="/">
        Go back
      </Link>
      {mealsOBJ.meals?.length !== 0 ? (
        <h1>{mealsOBJ.name}'s meals</h1>
      ) : (
        <h1>There is no meal added by {mealsOBJ.name}</h1>
      )}

      {/* <h3>{mealsOBJ.meals}</h3> */}
      <ListGroup className="center">
        {mealsOBJ.meals?.map((meal, index) => (
          <ListGroup.Item key={meal.id}>
            <Meal meal={meal} />
          </ListGroup.Item>
        ))}
      </ListGroup>
    </>
  );
};

export default PersonMealsScreen;
