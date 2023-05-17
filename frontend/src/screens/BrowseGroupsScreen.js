import React, { useState, useEffect } from "react";
import Group from "../components/Group";
import axios from "../axios";
import ListGroup from "react-bootstrap/ListGroup";

const BrowseGroupsScreen = () => {
  const [groups, setGroups] = useState([]);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const { data } = await axios.get("/api/v1/groups");

        setGroups(data);
      }
      catch (error) {
        console.log(error);
      }
    };

    fetchGroups();
  }, []);

  return (
    <>
      <h1>Groups</h1>
      <div className="d-flex justify-content-center">
        <ListGroup className="w-100">
          {groups.map((group) => (
            <ListGroup.Item key={group.id} className="border-0">
              <Group group={group}/>
            </ListGroup.Item>
          ))}
        </ListGroup>
      </div>
    </>
  );
};

export default BrowseGroupsScreen;
