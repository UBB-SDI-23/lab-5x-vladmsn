import React from 'react'

import { useState, useEffect } from 'react';
import axios from "../axios";

import { Link, useParams } from 'react-router-dom';


const UserGroupDetailScreen = () => {
  const [user, setUser] = useState({});
  const { id } = useParams();

  useEffect(() => {
    const fetchUser = async () => {
        try {
            const { data } = await axios.get(`/api/v1/users/details/${id}`);
            setUser(data);
        } catch (error) {
            console.log(error);
        }
    };

    fetchUser();
    }, []);


  return (
    <div></div>
  )
}

export default UserGroupDetailScreen;